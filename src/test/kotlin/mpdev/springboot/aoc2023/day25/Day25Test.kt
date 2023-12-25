package mpdev.springboot.aoc2023.day25

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day25.AoCInput
import mpdev.springboot.aoc2023.solutions.day25.Day25
import mpdev.springboot.aoc2023.solutions.day25.WiringDiagram
import mpdev.springboot.aoc2023.utils.InputUtils
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day25Test {

    private val day = 25                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day25()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var wiringDiagram: WiringDiagram

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        wiringDiagram = WiringDiagram(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(3)
    fun `Reads Input ans sets Wired Components`() {
        println("input transformed")
        inputLines.forEach { InputUtils(AoCInput::class.java).transform(it).println() }
        println("input to json")
        inputLines.map { InputUtils(AoCInput::class.java).transform(it) }
            .filterNot { InputUtils.skipEmptyLines && it.isEmpty() }
            .joinToString(",", "[", "]") { InputUtils(AoCInput::class.java).toJson(it) }
            .println()
        wiringDiagram.graph.forEach { it.println() }
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("54")
    }


    @Test
    @Order(8)
    fun `Solves Part 2`() {
        // NA - assertThat(puzzleSolver.solvePart2().result).isEqualTo("47")
    }
}
