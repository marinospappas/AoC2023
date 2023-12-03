package mpdev.springboot.aoc2023.day03

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day03.Day03
import mpdev.springboot.aoc2023.solutions.day03.Engine
import mpdev.springboot.aoc2023.utils.Point
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day03Test {

    private val day = 3                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day03()                      ///////// Update this for a new dayN test
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
        val engine = Engine(inputLines)
        engine.grid.print()
        assertThat(engine.grid.getDataPoints().count()).isEqualTo(100)
        assertThat(engine.grid.getDimensions()).isEqualTo(Pair(10,10))
    }

    @Test
    @Order(3)
    fun `Advances the Current Point to the Next Number`() {
        val engine = Engine(inputLines)
        var current = engine.grid.firstPoint()
        current = engine.advanceToNextNumber(current).also { it.println() }
        assertThat(current).isEqualTo(Point(0,0))
        current = engine.advanceToNextNumber(Point(3,0)).also { it.println() }
        assertThat(current).isEqualTo(Point(5,0))
        current = engine.advanceToNextNumber(Point(1,8)).also { it.println() }
        assertThat(current).isEqualTo(Point(1,9))
        current = engine.advanceToNextNumber(Point(8,9)).also { it.println() }
        assertThat(current).isEqualTo(Point(0,10))
    }

    @Test
    @Order(4)
    fun `Identifies Numbers Adjacent to Symbols Correctly`() {
        val engine = Engine(inputLines)
        val n1 = engine.getNextNumber(Point(0,0)).also { it.println() }
        engine.isAdjacentToSymbol(n1.second).println()
        engine.getAllAdjacent(n1.second).println()
        assertThat(n1.first).isEqualTo("467")
        assertThat(n1.second.size).isEqualTo(3)
        assertThat(engine.getAllAdjacent(n1.second).size).isEqualTo(12)
        assertTrue(engine.isAdjacentToSymbol(n1.second))
        val n2 = engine.getNextNumber(Point(0,5)).also { it.println() }
        engine.isAdjacentToSymbol(n2.second).println()
        engine.getAllAdjacent(n2.second).println()
        assertThat(n2.first).isEqualTo("58")
        assertThat(n2.second.size).isEqualTo(2)
        assertThat(engine.getAllAdjacent(n2.second).size).isEqualTo(10)
        assertFalse(engine.isAdjacentToSymbol(n2.second))
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("4361")
    }

    @Test
    @Order(6)
    fun `Identifies if a Number id Adjacent to a Gear`() {
        val engine = Engine(inputLines)
        val n = engine.getNextNumber(Point(0,0)).also { it.println() }
        val gears = engine.isAdjacentToGear(n.second).also { it.println() }
        assertThat(gears).isEqualTo(Pair(true, setOf(Point(3,1))))
        val n1 = engine.getNextNumber(Point(4,2)).also { it.println() }
        val gears1 = engine.isAdjacentToGear(n1.second).also { it.println() }
        assertThat(gears1).isEqualTo(Pair(false, setOf<Point>()))
        val n2 = engine.getNextNumber(Point(4,9)).also { it.println() }
        val gears2 = engine.isAdjacentToGear(n2.second).also { it.println() }
        assertThat(gears2).isEqualTo(Pair(true, setOf(Point(5,8), Point(8,9))))
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("467835")
    }
}
