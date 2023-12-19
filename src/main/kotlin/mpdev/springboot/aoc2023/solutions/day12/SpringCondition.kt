package mpdev.springboot.aoc2023.solutions.day12

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.AocInClass
import mpdev.springboot.aoc2023.utils.AocInField
import mpdev.springboot.aoc2023.utils.InputUtils
import kotlin.math.pow

@Serializable
@AocInClass
data class AoCInput(
    // ???.### 1,1,3
    // 0       1
    @AocInField(0) val s: String,
    @AocInField(1, delimiters = [","]) val numList: List<Int>
)

class SpringCondition(input: List<String>) {

    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    val records: List<Pair<String,List<Int>>> = aocInputList.map { Pair(it.s.replace(Regex("""\."""),"1").replace(Regex("""#"""),"0"), it.numList) }

    fun groupsToPattern(listLen: List<Int>): Regex {
        val regex = listLen.map { "0".repeat(it) }.joinToString("1+")
        return Regex("1*${regex}1*")
    }

    fun getMatchingCount(rec: Pair<String,List<Int>>): Int {
        val pattern = groupsToPattern(rec.second)
        val str = rec.first
        var count = 0
        val size = str.count { it == '?' }
        for (n in 0 until 2.0.pow(size).toInt()) {
            val digitList = n.toString(2).padStart(size, '0')
            var indx = 0
            val currTry = CharArray(str.length) { ' ' }
            for (i in str.indices) {
                currTry[i] =
                    if (str[i] == '?') digitList[indx++]
                    else str[i]
            }
            if (currTry.joinToString("").matches(pattern))
                ++count
        }
        return count
    }

    fun getMatchingCountDp(dpState: MutableMap<Triple<Int,Int,Int>,Long>, string: String, groupsOf0: List<Int>,
                           sIndx: Int, gIndex: Int, cur0Count: Int): Long {
        val key = Triple(sIndx, gIndex, cur0Count)
        if (dpState[key] != null)
            return dpState[key]!!
        if (sIndx == string.length)
            return if ((gIndex == groupsOf0.size && cur0Count == 0)
                || (gIndex == groupsOf0.size - 1 && groupsOf0[gIndex] == cur0Count)) 1L
            else 0L

        var matchingCount = 0L
        val c = string[sIndx]
        // outside a 0-group, while processing a series of 1s, '? also matched as 1, advance string ptr, 0-count must be 0
        if ((c == '1' || c == '?') && cur0Count == 0)
            matchingCount += getMatchingCountDp(dpState, string, groupsOf0, sIndx + 1, gIndex, 0)
        // encountered 1 just after a matched 0-group, '? also matched as 1, advance string ptr, 0-group ptr, reset 0-count
        else if ((c == '1' || c == '?') && cur0Count > 0 && gIndex < groupsOf0.size && groupsOf0[gIndex] == cur0Count)
            matchingCount += getMatchingCountDp(dpState, string, groupsOf0, sIndx + 1, gIndex + 1, 0)
        // within a 0-group, '?' also matched as 0, advance string ptr and 0-count
        if (c == '0' || c == '?')
            matchingCount += getMatchingCountDp(dpState, string, groupsOf0, sIndx + 1, gIndex, cur0Count + 1)

        dpState[key] = matchingCount
        return matchingCount
    }

    fun getMathingCount(rec: Pair<String,List<Int>>) =
        getMatchingCountDp(mutableMapOf(), rec.first, rec.second, 0, 0, 0)
}

operator fun Pair<String,List<Int>>.times(n: Int): Pair<String,List<Int>> {
    var str = ""
    val lst = mutableListOf<Int>()
    repeat(n-1) {
        str += "${this.first}?"
        lst.addAll(this.second)
    }
    return Pair(str+this.first, lst+this.second)
}
