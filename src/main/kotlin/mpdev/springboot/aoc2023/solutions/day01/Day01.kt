package mpdev.springboot.aoc2023.solutions.day01

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import mpdev.springboot.aoc2023.utils.AocException
import mpdev.springboot.aoc2023.utils.InputClass
import mpdev.springboot.aoc2023.utils.InputField
import mpdev.springboot.aoc2023.utils.InputUtils
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
    private lateinit var aocInputList: List<AoCInputDay01>
    lateinit var inputStrings: List<String>


    override fun initSolver(): Pair<Long,String> {
        result = 0
        val elapsed = measureNanoTime {
            aocInputList = InputUtils(AoCInputDay01::class.java).readAoCInput(inputData)
            inputStrings = aocInputList.map { it.s }
        }
        return Pair(elapsed/1000, "micro-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureNanoTime {
            result = inputStrings.map { it.toCharArray().filter { d -> d.isDigit() } }
                .sumOf { it.first().digitToInt() * 10 + it.last().digitToInt() }
        }
        return PuzzlePartSolution(1, result.toString(), elapsed/1000, "micro-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureNanoTime {
            val nums = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
                              "1",   "2",   "3",     "4",    "5",    "6",   "7",     "8",     "9")
            result = inputStrings.sumOf { s ->
                val n1 =  s.findAnyOf(nums)?.second ?: throw AocException("unknown error")
                val n2 =  s.findLastAnyOf(nums)?.second ?: throw AocException("unknown error")
                (nums.indexOf(n1) % 9 + 1)* 10 + (nums.indexOf(n2) % 9 + 1)
            }
        }
        return PuzzlePartSolution(2, result.toString(), elapsed/1000, "micro-sec")
    }

    @Serializable
    @InputClass
    data class AoCInputDay01(
        @InputField(0)  val s: String
    )
}