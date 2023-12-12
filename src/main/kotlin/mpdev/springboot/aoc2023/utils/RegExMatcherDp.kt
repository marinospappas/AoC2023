package mpdev.springboot.aoc2023.utils


object RegExMatcherDp {

    private lateinit var dp: Array<Array<Int?>>

    fun isMatch(text: String, pattern: String): Boolean {
        dp = Array(text.length) { Array(pattern.length) { null } }
        return isMatch(text, pattern, 0, 0)
    }

    private fun isMatch(text: String, pattern: String, ti: Int, pi: Int): Boolean {
        if (pi == pattern.length) return ti == text.length
        if (ti == text.length) {
            return isNextCharStar(pattern, pi) && isMatch(text, pattern, ti, pi + 2)
        }
        if (dp[ti][pi] != null) return dp[ti][pi] == 1
        var result: Boolean
        val hasFirstMatched = text[ti] == pattern[pi] || pattern[pi] == '.'
        if (isNextCharStar(pattern, pi)) {
            result = isMatch(text, pattern, ti, pi + 2)
            if (hasFirstMatched) {
                result = result || isMatch(text, pattern, ti + 1, pi)
            }
            dp[ti][pi] = if (result) 1 else 0
            return result
        }
        dp[ti][pi] = if (hasFirstMatched && isMatch(text, pattern, ti + 1, pi + 1)) 1 else 0
        return dp[ti][pi] == 1
    }

    private fun isNextCharStar(s: String, indx: Int) = indx < s.lastIndex && s[indx + 1] == '*'
}