package mpdev.springboot.aoc2023.day24

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day24.AoCInput
import mpdev.springboot.aoc2023.solutions.day24.Day24
import mpdev.springboot.aoc2023.solutions.day24.HailStones
import mpdev.springboot.aoc2023.utils.InputUtils
import mpdev.springboot.aoc2023.utils.LinearEqSys
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day24Test {

    private val day = 24                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day24()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var hailStones: HailStones

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        hailStones = HailStones(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(3)
    fun `Reads Input ans sets Hailstones List`() {
        println("input transformed")
        inputLines.forEach { InputUtils(AoCInput::class.java).transform(it).println() }
        println("input to json")
        inputLines.map { InputUtils(AoCInput::class.java).transform(it) }
            .filterNot { InputUtils.skipEmptyLines && it.isEmpty() }
            .joinToString(",", "[", "]") { InputUtils(AoCInput::class.java).toJson(it) }
            .println()
        hailStones.stones.forEach { it.println() }
    }

    @Test
    @Order(3)
    fun `Calculates Stones Intersections`() {
        hailStones.testArea = LongRange(7,27)
        hailStones.debug = true
        val result = hailStones.calculateIntersections().also { it.println() }
        assertThat(result).isEqualTo(2)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        puzzleSolver.hailStones.testArea = LongRange(7,27)
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("2")
    }

    @Test
    @Order(6)
    fun `Calculates Throw Position and Speed`() {
        val `throw` = hailStones.calculateThrow().also { it.println() }
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        puzzleSolver.solvePart1()
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("47")
    }
}
