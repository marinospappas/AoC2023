package mpdev.springboot.aoc2023.day11

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day11.Day11
import mpdev.springboot.aoc2023.solutions.day11.Universe
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day11Test {

    private val day = 11                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day11()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var universe: Universe

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        universe = Universe(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(3)
    fun `Reads Input ans sets Universe`() {
        universe.grid.print()
        universe.grid.getMinMaxXY().println()
        assertThat(universe.grid.getDataPoints().count()).isEqualTo(9)
        assertThat(universe.grid.getDimensions()).isEqualTo(Pair(10L,10L))
    }

    @Test
    @Order(3)
    fun `Expands universe by factor 2`() {
        universe.expandBy2()
        universe.grid.print()
        universe.grid.getMinMaxXY().println()
        assertThat(universe.grid.getDimensions()).isEqualTo(Pair(13L,12L))
    }

    @Test
    @Order(4)
    fun `Measures Distances after Expansion(2)`() {
        universe.expandBy2()
        universe.grid.print()
        val distances = universe.measureDistances().also { it.println() }
        distances.sum().println()
        assertThat(distances.size).isEqualTo(36)
        assertThat(distances.sum()).isEqualTo(374)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("374")
    }

    @ParameterizedTest
    @CsvSource(value = ["10, 1030", "100, 8410"])
    @Order(6)
    fun `Measures Distances after expansion x10, x100`(factor: Int, expected: Long) {
        universe.expand(factor)
        assertThat(universe.measureDistances().sum().also { it.println() }).isEqualTo(expected)
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("82000210")
    }
}
