package mpdev.springboot.aoc2023.utils


object RegExMatcherDp {

    fun isMatch(text: String, pattern: String): Boolean {
        return isMatch(mutableMapOf(), text, pattern, 0, 0)
    }

    private fun isMatch(dp: MutableMap<Pair<Int,Int>,Boolean>, text: String, pattern: String, ti: Int, pi: Int): Boolean {
        if (pi == pattern.length) return ti == text.length
        if (ti == text.length) {
            return isNextCharStar(pattern, pi) && isMatch(dp, text, pattern, ti, pi + 2)
        }
        if (dp[Pair(ti,pi)] != null) return dp[Pair(ti,pi)] == true
        var result: Boolean
        val hasFirstMatched = text[ti] == pattern[pi] || pattern[pi] == '.'
        if (isNextCharStar(pattern, pi)) {
            result = isMatch(dp, text, pattern, ti, pi + 2)
            if (hasFirstMatched) {
                result = result || isMatch(dp, text, pattern, ti + 1, pi)
            }
            dp[Pair(ti,pi)] = result
            return result
        }
        dp[Pair(ti,pi)] = hasFirstMatched && isMatch(dp, text, pattern, ti + 1, pi + 1)
        return dp[Pair(ti,pi)] == true
    }

    private fun isNextCharStar(s: String, index: Int) = index < s.lastIndex && s[index + 1] == '*'
}