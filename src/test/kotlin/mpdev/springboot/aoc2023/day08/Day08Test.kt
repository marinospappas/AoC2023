package mpdev.springboot.aoc2023.day08

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day08.AoCInput
import mpdev.springboot.aoc2023.solutions.day08.Day08
import mpdev.springboot.aoc2023.solutions.day08.InstructionMap
import mpdev.springboot.aoc2023.utils.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import java.io.File

class Day08Test {

    private val day = 8                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day08()                      ///////// Update this for a new dayN test
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
    @Order(3)
    fun `Reads Input ans sets Cards List`() {
        val instructionMap = InstructionMap(inputLines)
        println("input transformed")
        inputLines.forEach { InputUtils(AoCInput::class.java).transform(it).println() }
        println("input to json")
        inputLines.subList(1, inputLines.size)
            .map { InputUtils(AoCInput::class.java).transform(it) }
            .filterNot { InputUtils.skipEmptyLines && it.isEmpty() }
            .joinToString(",", "[", "]") { InputUtils(AoCInput::class.java).toJson(it) }
            .println()
        instructionMap.instructions.println()
        instructionMap.steps.forEach { it.println() }
        assertThat(instructionMap.instructions).isEqualTo(listOf('R','L'))
        assertThat(instructionMap.steps.size).isEqualTo(7)
    }

    @Test
    @Order(3)
    fun `Follows Instructions and counts Steps`() {
        val instructionMap = InstructionMap(inputLines)
        instructionMap.debug = true
        val steps1 = instructionMap.followSteps("AAA") { s -> s == "ZZZ" }.also { it.println() }
        assertThat(steps1).isEqualTo(2)
        val input = File("src/test/resources/inputdata/input08_1.txt").readLines()
        val instructionMap2 = InstructionMap(input)
        instructionMap2.debug = true
        val steps2 = instructionMap2.followSteps("AAA") { s -> s == "ZZZ" }.also { it.println() }
        assertThat(steps2).isEqualTo(6)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        puzzleSolver.inputData = File("src/test/resources/inputdata/input08_1.txt").readLines()
        puzzleSolver.initSolver()
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("6")
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        puzzleSolver.inputData = File("src/test/resources/inputdata/input08_2.txt").readLines()
        puzzleSolver.initSolver()
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("6")
    }

    @Test
    fun testx() {
        setOf(12599,17873,21389,17287,13771,15529L).gcd().println()
        setOf(12599,17873,21389,17287,13771,15529L).lcm().println()
        48L.primeFactors().println()
        180L.primeFactors().println()
    }
}
