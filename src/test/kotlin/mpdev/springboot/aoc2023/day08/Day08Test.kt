package mpdev.springboot.aoc2023.day08

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day08.Day08
import mpdev.springboot.aoc2023.solutions.day08.Xxxx
import mpdev.springboot.aoc2023.utils.println
import mpdev.springboot.aoc2023.utils.toJson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

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

   /* @Test
    @Order(2)
    fun `Deserializes Input`() {
        // generate and print json message from input
        inputLines.map { it.toJson(ScratchCard::class.java) }.also { it.println() }
        // convert input to json, deserialize and print
        Json.decodeFromString<List<ScratchCard>>(
            inputLines.joinToString(",", "[", "]") {  it.toJson(ScratchCard::class.java) }
        ).onEach { c -> c.println() }
    }*/

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
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("13")
    }

    @Test
    @Order(6)
    fun `Identifies Card Copies Won by a Winning Card`() {
        val xxxx = Xxxx(inputLines)

    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("30")
    }
}
