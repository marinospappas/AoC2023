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
        val nums = listOf( "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        val elapsed = measureNanoTime {
            result = inputData
                .sumOf { s -> findFirstNumber(s, nums) * 10 + findFirstNumber(s.reversed(), nums.map { n -> n.reversed() }) }
        }
        return PuzzlePartSolution(2, result.toString(), elapsed/1000, "micro-sec")
    }

    fun findFirstNumber(input: String, nums: List<String>): Int {
        var s = input
        while (s.isNotEmpty()) {
            if (s.first().isDigit())
                return s.first().digitToInt()
            else {
                for (n in nums) {
                    if (s.startsWith(n))
                        return nums.indexOf(n) + 1
                }
            }
            s = s.substring(1, s.length)
        }
        throw AocException("no number was found in [$input]")
    }

}