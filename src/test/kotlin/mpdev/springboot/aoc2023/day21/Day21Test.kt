package mpdev.springboot.aoc2023.day21

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day21.Day21
import mpdev.springboot.aoc2023.solutions.day21.FarmPlan
import mpdev.springboot.aoc2023.solutions.day21.FarmPlot
import mpdev.springboot.aoc2023.utils.Point
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import java.io.File

class Day21Test {

    private val day = 21                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day21()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var farmPlan: FarmPlan

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        farmPlan = FarmPlan(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(3)
    fun `Reads Input ans sets Farm Plan`() {
        farmPlan.grid.print()
        farmPlan.start.println()
        assertThat(farmPlan.grid.getDimensions()).isEqualTo(Pair(11,11))
        assertThat(farmPlan.start).isEqualTo(Point(5,5))
    }

    @Test
    @Order(3)
    fun `Traverses Farm`() {
        val border = farmPlan.traverseGrid(farmPlan.start, 6).also { it.println() }
        border.size.println()
        border.forEach { farmPlan.grid.setDataPoint(it, FarmPlot.BORDER) }
        farmPlan.grid.print()
        assertThat(border.size).isEqualTo(15)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("42")
    }

    @Test
    @Order(6)
    fun `Traverses Infinite Farm`() {
        inputLines = File("src/test/resources/inputdata/input21_1.txt").readLines()
        farmPlan = FarmPlan(inputLines)
        val border = farmPlan.traverseGrid(farmPlan.start, 21).also { it.println() }
        border.size.println()
        border.forEach { farmPlan.grid.setDataPoint(it, FarmPlot.BORDER) }
        farmPlan.grid.print()
        println("total:  ${farmPlan.grid.getDimensions().let { it.first * it.second }}")
        println("rocks:  ${farmPlan.grid.countOf(FarmPlot.ROCK)}")
        println("border: ${farmPlan.grid.countOf(FarmPlot.BORDER)}")
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        // NA - assertThat(puzzleSolver.solvePart2().result).isEqualTo("")
    }
}
