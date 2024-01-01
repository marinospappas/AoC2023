package mpdev.springboot.aoc2023.day17

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day17.CityMap
import mpdev.springboot.aoc2023.solutions.day17.Day17
import mpdev.springboot.aoc2023.utils.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import java.io.File

class Day17Test {

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
        cityMap.graph.nodes.forEach { it.println() }
        assertThat(cityMap.grid.getDataPoints().size).isEqualTo(13 * 13)
        assertThat(cityMap.grid.getDimensions()).isEqualTo(Pair(13,13))
    }

    @Test
    @Order(3)
    fun `Finds Min Loss Path`() {
        val result: MinCostPath<CityMap.GraphState> = cityMap.findMinPath().also { it.print() }
        assertThat(result.minCost).isEqualTo(102)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("102")
    }

    @Test
    @Order(6)
    fun `Finds Min Loss Path with Additional Restriction`() {
        inputLines = File("src/test/resources/inputdata/input17_1.txt").readLines()
        cityMap = CityMap(inputLines)
        cityMap.minStraightSteps = 4
        cityMap.maxStraightSteps = 10
        val result: MinCostPath<CityMap.GraphState> = cityMap.findMinPath().also { it.print() }
        assertThat(result.minCost).isEqualTo(71)
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        puzzleSolver.cityMap.findMinPath().print()
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("94")
    }

    private fun MinCostPath<CityMap.GraphState>.print() {
        println("path,cost: $path")
        println("min cost: $minCost")
        Grid(path.map { it.first.point }.toTypedArray(), mapOf('x' to 0), border = 0).print()
    }
}
