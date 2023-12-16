package mpdev.springboot.aoc2023.day16

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day16.Beam
import mpdev.springboot.aoc2023.solutions.day16.Day16
import mpdev.springboot.aoc2023.solutions.day16.LavaFloor
import mpdev.springboot.aoc2023.utils.GridUtils
import mpdev.springboot.aoc2023.utils.Point
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day16Test {

    private val day = 16                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day16()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var lavaFloor: LavaFloor

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        lavaFloor = LavaFloor(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(3)
    fun `Reads Input ans sets Grid of Mirrors`() {
        lavaFloor.grid.print()
        assertThat(lavaFloor.grid.getDataPoints().size).isEqualTo(23)
        assertThat(lavaFloor.grid.getDimensions()).isEqualTo(Pair(10,10))
    }

    @Test
    @Order(3)
    fun `Simulates Beam movement`() {
        lavaFloor.debug = true
        val result = lavaFloor.simulateBeam(Beam(Point(0,0), GridUtils.Direction.RIGHT)).also { it.println() }
        assertThat(result).isEqualTo(46)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("46")
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("51")
    }
}
