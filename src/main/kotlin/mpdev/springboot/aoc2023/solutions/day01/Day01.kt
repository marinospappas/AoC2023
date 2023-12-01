package mpdev.springboot.aoc2023.solutions.day01

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import mpdev.springboot.aoc2023.utils.AocException
import org.springframework.stereotype.Component
import kotlin.system.measureNanoTime

@Component
class Day01: PuzzleSolver() {

    final override fun setDay() {
        day = 1
    }

    init {
        setDay()
    }

    var result = 0

    override fun initSolver(): Pair<Long,String> {
        result = 0
        val elapsed = measureNanoTime {}
        return Pair(elapsed/1000, "micro-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureNanoTime {
            result = inputData.map { it.toCharArray().filter { d -> d.isDigit() } }
                .sumOf { it.first().digitToInt() * 10 + it.last().digitToInt() }
        }
        return PuzzlePartSolution(1, result.toString(), elapsed/1000, "micro-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val nums = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9,
            "1" to 1, "2" to 2, "3" to 3, "4" to 4, "5" to 5, "6" to 6, "7" to 7, "8" to 8, "9" to 9)
        val numsReversed = nums.entries.associate { e -> e.key.reversed() to e.value }      // "one" becomes "eno", "two becomes "owt", etc...
        val elapsed = measureNanoTime {
            result = inputData.sumOf { s -> firstNumber(s, nums) * 10 + firstNumber(s.reversed(), numsReversed) }
        }
        return PuzzlePartSolution(2, result.toString(), elapsed/1000, "micro-sec")
    }

    fun firstNumber(input: String, numbers: Map<String,Int>): Int {
        var s = input
        while (s.isNotEmpty()) {
            numbers.forEach { (n, i) -> if (s.startsWith(n)) return i }
            s = s.substring(1, s.length)
        }
        throw AocException("no number was found in [$input]")
        //  more compact but slower
        //  return numbers.entries.associate { e -> s.indexOf(e.key) to e.value }
        //            .entries.sortedBy { it.key }.first { e -> e.key >= 0 }.value
    }

}