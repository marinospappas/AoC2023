package mpdev.springboot.aoc2023.day09

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day09.Day09
import mpdev.springboot.aoc2023.solutions.day09.Xxxx
import mpdev.springboot.aoc2023.utils.println
import mpdev.springboot.aoc2023.utils.toJson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day09Test {

    private val day = 9                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day09()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
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

    /*@Test
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
    fun `Reads Input ans sets `() {

    }

    @Test
    @Order(3)
    fun `Identifies `() {

    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("13")
    }

    @Test
    @Order(6)
    fun `Identifies 2`() {

    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("30")
    }
}
