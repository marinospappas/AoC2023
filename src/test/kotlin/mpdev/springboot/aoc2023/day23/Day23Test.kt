package mpdev.springboot.aoc2023.day23

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day23.Day23
import mpdev.springboot.aoc2023.solutions.day23.TrailMap
import mpdev.springboot.aoc2023.utils.allPaths
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day23Test {

    private val day = 23                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day23()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var trailMap: TrailMap

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        trailMap = TrailMap(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(3)
    fun `Reads Input ans sets Trail Paths Map`() {
        trailMap.grid.print()
        println("start: ${trailMap.start}, end: ${trailMap.end}")
        assertThat(trailMap.grid.getDimensions()).isEqualTo(Pair(23,23))
        assertThat(trailMap.start.y).isEqualTo(0)
        assertThat(trailMap.end.y).isEqualTo(22)
    }

    @Test
    @Order(3)
    fun `Identifies All Possible Paths`() {
        //  94 steps. (The other possible hikes you could have taken were 90, 86, 82, 82, and 74
        val graph = trailMap.initGraph(1)
        val paths = graph.allPaths(trailMap.start, trailMap.end).map { it.size -1 }.also { it.println() }
        val maxSteps = trailMap.findMaxPath1().also { it.println() }
        assertThat(paths.sorted()).isEqualTo(listOf(74, 82, 82, 86, 90, 94))
        assertThat(maxSteps).isEqualTo(94)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("94")
    }

    @Test
    @Order(6)
    fun `Identifies Max Path - part 2`() {
        val maxSteps = trailMap.findMaxPath2().also { it.println() }
        assertThat(maxSteps).isEqualTo(154)
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("154")
    }
}
