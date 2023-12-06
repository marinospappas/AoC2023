package mpdev.springboot.aoc2023.day07

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day07.Day07
import mpdev.springboot.aoc2023.solutions.day07.InputMapped
import mpdev.springboot.aoc2023.solutions.day07.Xxxx
import mpdev.springboot.aoc2023.utils.println
import mpdev.springboot.aoc2023.utils.toJson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import kotlin.system.measureNanoTime

class Day07Test {

    private val day = 7                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day07()                      ///////// Update this for a new dayN test
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
    fun `Deserializes Input`() {
        // generate and print json message from input
        inputLines.map { it.toJson(InputMapped::class.java) }.also { it.println() }
        // convert input to json, deserialize and print
        Json.decodeFromString<List<InputMapped>>(
            inputLines.joinToString(",", "[", "]") {  it.toJson(InputMapped::class.java) }
        ).onEach { c -> c.println() }
    }

    @Test
    @Order(3)
    fun `Reads Input ans sets Cards List`() {
        val xxxx = Xxxx(inputLines)

    }

    @Test
    @Order(3)
    fun `Identifies Winning Numbers and Points in a Card`() {
        val xxxx = Xxxx(inputLines)

    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("     ")
    }

    @Test
    @Order(6)
    fun `Identifies Card Copies Won by a Winning Card`() {
        val xxxx = Xxxx(inputLines)

    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("      ")
    }
}
