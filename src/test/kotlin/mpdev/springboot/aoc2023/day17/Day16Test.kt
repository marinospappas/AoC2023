package mpdev.springboot.aoc2023.day17

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day17.CityMap
import mpdev.springboot.aoc2023.solutions.day17.Day17
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day16Test {

    private val day = 17                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day17()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var cityMap: CityMap

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        cityMap = CityMap(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(3)
    fun `Reads Input ans sets City Grid`() {
        cityMap.grid.print()
        assertThat(cityMap.grid.getDataPoints().size).isEqualTo(13 * 13)
        assertThat(cityMap.grid.getDimensions()).isEqualTo(Pair(13,13))
    }

    @Test
    @Order(3)
    fun `Find Min Heat Loss Path`() {
        val result = cityMap.findMinPath()
        result.path.forEach { it.println() }
        result.minCost.println()
        result.numberOfIterations.println()
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("102")
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("94")
    }
}
