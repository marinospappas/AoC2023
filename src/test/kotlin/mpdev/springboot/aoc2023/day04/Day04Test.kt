package mpdev.springboot.aoc2023.day04

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day04.Day04
import mpdev.springboot.aoc2023.utils.Point
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day04Test {

    private val day = 4                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day04()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input ans sets Grid`() {

    }

    @Test
    @Order(3)
    fun `Advances the Current Point to the Next Number`() {

    }

    @Test
    @Order(4)
    fun `Identifies Numbers Adjacent to Symbols Correctly`() {

    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("4361")
    }

    @Test
    @Order(6)
    fun `Identifies if a Number id Adjacent to a Gear`() {

    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("467835")
    }
}
