package mpdev.springboot.aoc2023.day12

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day12.*
import mpdev.springboot.aoc2023.utils.InputUtils
import mpdev.springboot.aoc2023.utils.RegExMatcher.TokenType.*
import mpdev.springboot.aoc2023.utils.RegExMatcher.MatchToken
import mpdev.springboot.aoc2023.utils.RegExMatcher.Regex
import mpdev.springboot.aoc2023.utils.match
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day12Test {

    private val day = 12                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day12()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var springCondition: SpringCondition

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        springCondition = SpringCondition(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input and sets Patterns`() {
        println("input transformed")
        inputLines.forEach { InputUtils(AoCInput::class.java).transform(it).println() }
        println("input to json")
        inputLines.map { InputUtils(AoCInput::class.java).transform(it) }
            .filterNot { InputUtils.skipEmptyLines && it.isEmpty() }
            .joinToString(",", "[", "]") { InputUtils(AoCInput::class.java).toJson(it) }
            .println()
        springCondition.records.forEach { it.println() }
        assertThat(springCondition.records.size).isEqualTo(6)
    }

    @Test
    @Order(3)
    fun `Finds Matching Combinations Brute Force`() {
        val expected = listOf(1L, 4, 1, 1, 4, 10)
        for (i in springCondition.records.indices) {
            val rec = springCondition.records[i]
            val result = springCondition.getMatchingCount(rec)
                .also { it.println() }
            assertThat(result).isEqualTo(expected[i])
        }
    }

    @Test
    @Order(4)
    fun `Finds Matching Combinations DP`() {
        val expected = listOf(1L, 4, 1, 1, 4, 10)
        for (i in springCondition.records.indices) {
            val rec = springCondition.records[i]
            val result = springCondition.getMatchingCountDp(mutableMapOf(), rec.first, rec.second, 0, 0, 0)
                .also { it.println() }
            assertThat(result).isEqualTo(expected[i])
        }
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("21")
    }

    @Test
    @Order(6)
    fun `Finds Extended Matching Combinations DP`() {
        val expected = listOf(1L, 16384, 1, 16, 2500, 506250)
        for (i in springCondition.records.indices) {
            val rec = springCondition.records[i] * 5
            val result = springCondition.getMatchingCountDp(mutableMapOf(), rec.first, rec.second, 0, 0, 0)
                .also { it.println() }
            assertThat(result).isEqualTo(expected[i])
        }
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
         assertThat(puzzleSolver.solvePart2().result).isEqualTo("525152")
    }
}
