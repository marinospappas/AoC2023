package mpdev.springboot.aoc2023.day14

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day14.AoCInput
import mpdev.springboot.aoc2023.solutions.day14.Day14
import mpdev.springboot.aoc2023.solutions.day14.Xxxx
import mpdev.springboot.aoc2023.utils.InputUtils
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day14Test {

    private val day = 14                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day14()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var xxxx: Xxxx

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        xxxx = Xxxx(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input ans sets Patterns`() {
        println("input transformed")
        inputLines.forEach { InputUtils(AoCInput::class.java).transform(it).println() }
        println("input to json")
        inputLines.forEach { InputUtils(AoCInput::class.java).toJson(it).println() }

    }

    @Test
    @Order(3)
    fun `Finds Matching Combinations Brute Force`() {

    }

    @Test
    @Order(4)
    fun `Finds Matching Combinations DP`() {

    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("21")
    }

    @Test
    @Order(6)
    fun `Finds Extended Matching Combinations DP`() {

    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
         assertThat(puzzleSolver.solvePart2().result).isEqualTo("525152")
    }
}
