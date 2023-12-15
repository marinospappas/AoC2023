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

    // TODO refactor the below to make it easier to read
    fun getMatchingCountDp(dpState: MutableMap<Triple<Int,Int,Int>,Long>, str: String, groups: List<Int>,
                           sIndx: Int, gIndex: Int, cur0Count: Int): Long {
        val key = Triple(sIndx, gIndex, cur0Count)
        if (dpState.containsKey(key))
            return dpState[key]!!

        if (sIndx == str.length)
            return if ((gIndex > groups.lastIndex && cur0Count == 0) ||
                (gIndex == groups.lastIndex && groups[gIndex] == cur0Count)) 1L
            else 0L

        var count = 0L
        val c = str[sIndx]
        count += when (c) {
            '1' ->  if (cur0Count == 0)     // already outside a 0 group
                        getMatchingCountDp(dpState, str, groups, sIndx+1, gIndex, 0)
                    else 0L
            '0' -> { 1 }
            '?' -> { 1 }
            else -> 0
        }
        if ((c == '1' || c == '?') && cur0Count == 0)
            count += getMatchingCountDp(dpState, str, groups, sIndx + 1, gIndex, 0)
        else if ((c == '1' || c == '?') && cur0Count > 0 && gIndex < groups.size && groups[gIndex] == cur0Count)
            count += getMatchingCountDp(dpState, str, groups, sIndx + 1, gIndex + 1, 0)
        if (c == '0' || c == '?')
            count += getMatchingCountDp(dpState, str, groups, sIndx + 1, gIndex, cur0Count + 1)

        dpState[key] = count
        return count
    }

    fun getCount(rec: Pair<String,List<Int>>) =
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