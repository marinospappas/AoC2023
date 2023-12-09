package mpdev.springboot.aoc2023.day10

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day10.AoCInput
import mpdev.springboot.aoc2023.solutions.day10.Day10
import mpdev.springboot.aoc2023.solutions.day10.Xxxx
import mpdev.springboot.aoc2023.utils.InputUtils
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day10Test {

    private val day = 10                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day10()                      ///////// Update this for a new dayN test
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
    @Order(3)
    fun `Reads Input ans sets `() {
        println("input transformed")
        inputLines.forEach { InputUtils(AoCInput::class.java).transform(it).println() }
        println("input to json")
        inputLines.forEach { InputUtils(AoCInput::class.java).toJson(it).println() }
        //xxxx. .forEach { it.println() }
        //assertThat(oasis.failures.size).isEqualTo(3)
        //assertTrue(oasis.failures.all { it.size == 6 })
    }

    @Test
    @Order(3)
    fun `Predicts next value`() {

    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("114")
    }

    @Test
    @Order(6)
    fun `Extrapolates previous value`() {

    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("2")
    }
}
