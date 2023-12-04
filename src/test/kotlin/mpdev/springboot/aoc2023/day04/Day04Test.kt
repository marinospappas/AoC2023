package mpdev.springboot.aoc2023.day04

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day04.ScratchCardGame.Companion.toJson
import mpdev.springboot.aoc2023.solutions.day04.Day04
import mpdev.springboot.aoc2023.solutions.day04.ScratchCard
import mpdev.springboot.aoc2023.solutions.day04.ScratchCardGame
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day04Test {

    private val day = 4                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day04()                      ///////// Update this for a new dayN test
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
        val c1 = ScratchCard(listOf(1,2,3,4,5), listOf(9,10,11,12))
        val c2 = ScratchCard(listOf(8,9,10,11), listOf(20,21,23,32))
        val map = mapOf(1 to c1, 2 to c2)
        Json.encodeToString(map).also { it.println() }
        val input = listOf( "Card   3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
            "Card  2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19"
        )
        val json = input.joinToString(",", "{", "}") { it.toJson() }
            .also { it.println() }
        Json.decodeFromString<Map<Int, ScratchCard>>(json).also { it.println() }
    }

    @Test
    @Order(3)
    fun `Reads Input ans sets Cards List`() {
        val scratchCardGame = ScratchCardGame(inputLines)
        scratchCardGame.cards.forEach { it.println()}
        assertThat(scratchCardGame.cards.size).isEqualTo(6)
        assertThat(scratchCardGame.cards.keys).isEqualTo(setOf(1,2,3,4,5,6))
    }

    @Test
    @Order(3)
    fun `Identifies Winning Numbers and Points in a Card`() {
        val expected = listOf(4, 2, 2, 1, 0, 0)
        val expected1 = listOf(8, 2, 2, 1, 0, 0)
        val scratchCardGame = ScratchCardGame(inputLines)
        var i = 0
        scratchCardGame.cards.values.forEach { card ->
            println("card ${i+1}")
            val countOfWinningNums = card.countOfWinningNums().also { it.println() }
            val points = card.points().also { it.println() }
            assertThat(countOfWinningNums).isEqualTo(expected[i])
            assertThat(points).isEqualTo(expected1[i++])
        }
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("13")
    }

    @Test
    @Order(6)
    fun `Identifies Card Copies Won by a Winning Card`() {

    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("30")
    }
}
