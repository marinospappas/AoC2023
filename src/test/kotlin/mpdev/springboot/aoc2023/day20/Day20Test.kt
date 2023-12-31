package mpdev.springboot.aoc2023.day20

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day20.*
import mpdev.springboot.aoc2023.utils.InputUtils
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.io.File

class Day20Test {

    private val day = 20                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day20()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var pulseProcessor: PulseProcessor

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        pulseProcessor = PulseProcessor(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(3)
    fun `Reads Input ans sets Modules and Wiring`() {
        println("input transformed")
        inputLines.forEach { InputUtils(AoCInput::class.java).transform(it).println() }
        println("input to json")
        inputLines.map { InputUtils(AoCInput::class.java).transform(it) }
            .filterNot { InputUtils.skipEmptyLines && it.isEmpty() }
            .joinToString(",", "[", "]") { InputUtils(AoCInput::class.java).toJson(it) }
            .println()
        println("Modules")
        pulseProcessor.modules.forEach { it.println() }
    }

    @Test
    @Order(3)
    fun `Processes Pulse through the Circuit`() {
        pulseProcessor.debug = true
        val expected = listOf(
            Pair(4,4), Pair(4,2), Pair(5,3), Pair(4,2)
        )
        repeat(4) {
            println("*** cycle ${it+1}")
            val (l,h) = pulseProcessor.processPulse()
                .also { r -> r.println() }
            assertThat(Pair(l,h)).isEqualTo(expected[it])
        }
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("11687500")
    }

    @Test
    @Order(6)
    fun `Finds Pulse Cycle`() {
        inputLines = File("src/main/resources/inputdata/input20.txt").readLines()
        pulseProcessor = PulseProcessor(inputLines)
        pulseProcessor.debug2 = true
        assertDoesNotThrow {
            pulseProcessor.identifyHighPulseCyclesForFinalConjunction().also { it.println() }
        }
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        // NA - assertThat(puzzleSolver.solvePart2().result).isEqualTo("")
    }
}
