package mpdev.springboot.aoc2023.day10

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day10.*
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import java.io.File

class Day10Test {

    private val day = 10                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day10()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var pipeNetwork: PipeNetwork

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        pipeNetwork = PipeNetwork(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(3)
    fun `Reads Input and sets Pipe Network`() {
        pipeNetwork.grid.print()
        println("start: ${pipeNetwork.start}")
    }

    @Test
    @Order(3)
    fun `Finds Pipe Loop`() {
        pipeNetwork.findLoop()
        pipeNetwork.loop.forEach { it.println() }
        pipeNetwork.loop.size.println()
        assertThat(pipeNetwork.loop.size).isEqualTo(16)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("8")
    }

    // TODO: clean up part 2 tests
    @Test
    fun `Find Points Inside the Loop`() {
        pipeNetwork = PipeNetwork(File("src/test/resources/inputdata/input10_1.txt").readLines())
        pipeNetwork.grid.print()
        pipeNetwork.findLoop()
        var count = pipeNetwork.findPointsInsideLoop().also { it.println() }
        assertThat(count).isEqualTo(4)
        pipeNetwork = PipeNetwork(File("src/test/resources/inputdata/input10_3.txt").readLines())
        pipeNetwork.grid.print()
        pipeNetwork.findLoop()
        count = pipeNetwork.findPointsInsideLoop().also { it.println() }
        assertThat(count).isEqualTo(8)
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        puzzleSolver.inputData = File("src/test/resources/inputdata/input10_3.txt").readLines()
        puzzleSolver.initSolver()
        puzzleSolver.solvePart1()
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("8")
    }
}
