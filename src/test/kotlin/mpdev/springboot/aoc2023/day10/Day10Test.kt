package mpdev.springboot.aoc2023.day10

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day10.*
import mpdev.springboot.aoc2023.utils.Grid
import mpdev.springboot.aoc2023.utils.GridUtils
import mpdev.springboot.aoc2023.utils.Point
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import java.io.File

class Day10Test {

    private val day = 10                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day10()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/main/resources/inputdata/input")
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
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("114")
    }

    @Test
    fun `Test isInside Loop`() {
        pipeNetwork = PipeNetwork(File("src/main/resources/inputdata/input10.txt").readLines())
        pipeNetwork.grid.print()
        pipeNetwork.findLoop()

        pipeNetwork.loop[0] = Pair(pipeNetwork.start, Segment.VERT)
        val auxGrid = Grid(pipeNetwork.loop.associate { it.first to it.second }, Segment.mapper)
        auxGrid.print()
        auxGrid.getDataPoints().forEach { it.println() }

        val (minx, maxx, miny, maxy) = auxGrid.getMinMaxXY()
        var count = 0
        for (x in minx..maxx)
            for (y in miny .. maxy) {
                if (pipeNetwork.loop.map { it.first }.contains(Point(x,y)))
                    continue
                if(GridUtils.isInsideArea(auxGrid,Point(x,y), setOf(Segment.VERT, Segment.STOE, Segment.STOW)))
                    ++count
            }
        count.println()
    }

    @Test
    @Order(6)
    fun `Find Points Inside`() {
        //pipeNetwork.origGrid.setDataPoint(pipeNetwork.start, Segment.NTOE)  // Test
        //pipeNetwork.origGrid.setDataPoint(pipeNetwork.start, Segment.VERT)  // Prod
        pipeNetwork = PipeNetwork(File("src/test/resources/inputdata/input10.txt").readLines())
        pipeNetwork.grid.print()
        pipeNetwork.findLoop()
        pipeNetwork.identifyStartDatum(pipeNetwork.loop.map { it.first })

        val auxGrid = Grid(pipeNetwork.loop.associate { it.first to it.second }, Segment.mapper)
        auxGrid.print()

       pipeNetwork.findPointsInsideLoop().println()

    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("2")
    }
}
