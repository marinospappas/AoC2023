package mpdev.springboot.aoc2023.day22

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day22.AoCInput
import mpdev.springboot.aoc2023.solutions.day22.Day22
import mpdev.springboot.aoc2023.solutions.day22.JengaBricks
import mpdev.springboot.aoc2023.utils.InputUtils
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day22Test {

    private val day = 22                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day22()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var jengaBricks: JengaBricks

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        jengaBricks = JengaBricks(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(3)
    fun `Reads Input ans sets Bricks List`() {
        println("input transformed")
        inputLines.forEach { InputUtils(AoCInput::class.java).transform(it).println() }
        println("input to json")
        inputLines.map { InputUtils(AoCInput::class.java).transform(it) }
            .filterNot { InputUtils.skipEmptyLines && it.isEmpty() }
            .joinToString(",", "[", "]") { InputUtils(AoCInput::class.java).toJson(it) }
            .println()
        println("List of Bricks")
        jengaBricks.bricks.forEach { it.println() }
        assertThat(jengaBricks.bricks.size).isEqualTo(7)
    }

    @Test
    @Order(3)
    fun `Lands Bricks on Ground or on Other Bricks`() {
        jengaBricks.debug = true
        jengaBricks.landAllBricks()
        val removable = jengaBricks.getRemovableBricks().also { it.println() }
        assertThat(removable.size).isEqualTo(5)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("5")
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        puzzleSolver.solvePart1()
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("7")
    }
}
