package mpdev.springboot.aoc2023.solutions.day09

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.AocInClass
import mpdev.springboot.aoc2023.utils.AocInField
import mpdev.springboot.aoc2023.utils.InputUtils

@Serializable
@AocInClass
data class AoCInput(
    // 0 3 6 9 12 15
    // 0
    @AocInField(0, delimiters = [" "]) val values: List<Long>
)

class Oasis(input: List<String>) {

    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    val failures: List<List<Long>> = aocInputList.map { it.values }

    private fun List<Long>.elementDiffs() = (0 until this.lastIndex).map { this[it+1] - this[it] }

    fun predictNextValue(history: List<Long>): Long {
        var current = history.toList()
        val lastDigitList = mutableListOf(current.last())
        while (current.any { it != 0L })
            current = current.elementDiffs().also { lastDigitList.add(it.last()) }
        return lastDigitList.sum()
    }

    fun extrapolatePastValue(history: List<Long>): Long {
        var current = history.toList()
        val firstDigitList = mutableListOf(current.first())
        while (current.any { it != 0L })
            current = current.elementDiffs().also { firstDigitList.add(it.first()) }
        // for (d in firstDigitList.reversed()) result = d - result
        // the past value is the sum of all the first digits but with alternating sign (+, -, +, -, +, ...)
        var sign = -1
        return firstDigitList.sumOf { sign = -sign; sign * it }
    }
}