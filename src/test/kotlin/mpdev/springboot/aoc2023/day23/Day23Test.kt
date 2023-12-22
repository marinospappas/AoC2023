package mpdev.springboot.aoc2023.day23

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day23.AoCInput
import mpdev.springboot.aoc2023.solutions.day23.Day23
import mpdev.springboot.aoc2023.solutions.day23.Xxxx
import mpdev.springboot.aoc2023.utils.InputUtils
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day23Test {

    private val day = 23                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day23()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/main/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var xxxx: Xxxx

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        xxxx = Xxxx(inputLines)
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
    }

    @Test
    @Order(3)
    fun `Lands Bricks on Ground or on Other Bricks`() {

    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("5")
    }

    @Test
    @Order(6)
    fun `Simulates Collapse of Bricks`() {

    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("7")
    }
}
