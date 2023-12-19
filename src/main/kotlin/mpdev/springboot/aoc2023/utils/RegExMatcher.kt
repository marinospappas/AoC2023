package mpdev.springboot.aoc2023.utils

import mpdev.springboot.aoc2023.utils.RegExMatcher.TokenType.*

object RegExMatcher {

    fun isMatch(text: String, pattern: String): Boolean {
        return isMatch(mutableMapOf(), text, pattern, 0, 0)
    }

    private fun isMatch(
        dp: MutableMap<Pair<Int, Int>, Boolean>,
        text: String,
        pattern: String,
        ti: Int,
        pi: Int
    ): Boolean {
        if (pi == pattern.length) return ti == text.length
        if (ti == text.length) {
            return isNextCharStar(pattern, pi) && isMatch(dp, text, pattern, ti, pi + 2)
        }
        if (dp[Pair(ti, pi)] != null) return dp[Pair(ti, pi)] == true
        var result: Boolean
        val hasFirstMatched = text[ti] == pattern[pi] || pattern[pi] == '.'
        if (isNextCharStar(pattern, pi)) {
            result = isMatch(dp, text, pattern, ti, pi + 2)
            if (hasFirstMatched) {
                result = result || isMatch(dp, text, pattern, ti + 1, pi)
            }
            dp[Pair(ti, pi)] = result
            return result
        }
        dp[Pair(ti, pi)] = hasFirstMatched && isMatch(dp, text, pattern, ti + 1, pi + 1)
        return dp[Pair(ti, pi)] == true
    }

    private fun isNextCharStar(s: String, index: Int) = index < s.lastIndex && s[index + 1] == '*'

    class Regex(val pattern: List<MatchToken>)

    val dpState = mutableMapOf<Pair<Int, Int>, Boolean>()
    fun match(
        dpState: MutableMap<Pair<Int, Int>, Boolean>, str: String, pattern: List<MatchToken>, sIndex: Int, pIndex: Int
    ): Boolean {
        if (sIndex > str.lastIndex) {
            return (pIndex > pattern.lastIndex)
                    ||
                    (pIndex == pattern.lastIndex
                            && (pattern.last().type == OnceOrMore && pattern.last().countMatched >= 1 || pattern.last().type == ZeroOrMore))
        }
        if (pIndex > pattern.lastIndex)
            return false
        val c = str[sIndex]
        val token = pattern[pIndex]
        return when (token.type) {
            Exact ->
                if (c == token.char) {
                    ++pattern[pIndex].countMatched
                    match(dpState, str, pattern, sIndex + 1, pIndex + 1)
                } else false
            ZeroOrMore -> {
                if (c == token.char) {
                    ++pattern[pIndex].countMatched
                    match(dpState, str, pattern, sIndex + 1, pIndex)
                } else
                    match(dpState, str, pattern, sIndex, pIndex + 1)
            }
            OnceOrMore -> {
                if (c == token.char) {
                    ++pattern[pIndex].countMatched
                    match(dpState, str, pattern, sIndex + 1, pIndex)
                } else {
                    if (pattern[pIndex].countMatched >= 1)
                        match(dpState, str, pattern, sIndex, pIndex + 1)
                    else
                        false
                }
            }
        }
    }

    data class MatchToken(val char: Char, val type: TokenType = Exact, var countMatched: Int = 0)
    enum class TokenType {
        Exact,
        OnceOrMore,
        ZeroOrMore
    }
}

fun String.match(regex: RegExMatcher.Regex): Boolean {
    RegExMatcher.dpState.clear()
    return RegExMatcher.match(RegExMatcher.dpState, this, regex.pattern, 0, 0)
}