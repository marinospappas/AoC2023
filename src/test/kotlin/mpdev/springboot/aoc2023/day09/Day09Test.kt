package mpdev.springboot.aoc2023.day09

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day09.AoCInput
import mpdev.springboot.aoc2023.solutions.day09.Day09
import mpdev.springboot.aoc2023.solutions.day09.Oasis
import mpdev.springboot.aoc2023.utils.InputUtils
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day09Test {

    private val day = 9                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day09()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var oasis: Oasis

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        oasis = Oasis(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(3)
    fun `Reads Input ans sets Failures data`() {
        oasis.failures.forEach { it.println() }
        println("input transformed")
        inputLines.forEach { InputUtils(AoCInput::class.java).transform(it).println() }
        println("input to json")
        inputLines.forEach { InputUtils(AoCInput::class.java).toJson(it).println() }
        assertThat(oasis.failures.size).isEqualTo(3)
        assertTrue(oasis.failures.all { it.size == 6 })
    }

    @Test
    @Order(3)
    fun `Predicts next value`() {
        val expected = listOf(18L, 28L, 68L)
        oasis.failures.indices.forEach { i ->
            val next = oasis.predictNextValue(oasis.failures[i]).also { it.println() }
            assertThat(next).isEqualTo(expected[i])
        }
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("114")
    }

    @Test
    @Order(6)
    fun `Extrapolates previous value`() {
        val expected = listOf(-3L, 0L, 5L)
        oasis.failures.indices.forEach { i ->
            val next = oasis.extrapolatePastValue(oasis.failures[i]).also { it.println() }
            assertThat(next).isEqualTo(expected[i])
        }
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("2")
    }
}
