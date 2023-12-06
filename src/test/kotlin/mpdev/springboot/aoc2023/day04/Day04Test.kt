package mpdev.springboot.aoc2023.day04

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day04.Day04
import mpdev.springboot.aoc2023.solutions.day04.ScratchCard
import mpdev.springboot.aoc2023.solutions.day04.ScratchCardGame
import mpdev.springboot.aoc2023.utils.println
import mpdev.springboot.aoc2023.utils.toJson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import kotlin.system.measureNanoTime

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
        // generate and print json message from input
        inputLines.map { it.toJson(ScratchCard::class.java) }.also { it.println() }
        // convert input to json, deserialize and print
        Json.decodeFromString<List<ScratchCard>>(
            inputLines.joinToString(",", "[", "]") {  it.toJson(ScratchCard::class.java) }
        ).onEach { c -> c.println() }
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
        scratchCardGame.cards.forEach { (id, card) ->
            println("card $id")
            val countOfWinningNums = card.winningCount.also { it.println() }
            val points = card.points().also { it.println() }
            assertThat(countOfWinningNums).isEqualTo(expected[id-1])
            assertThat(points).isEqualTo(expected1[id-1])
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
        val expected = listOf(listOf(2,3,4,5), listOf(3,4), listOf(4,5), listOf(5), emptyList(), emptyList())
        val scratchCardGame = ScratchCardGame(inputLines)
        val elapsedNoCache = measureNanoTime {
            scratchCardGame.cards.forEach { (id, card) ->
                println("card $id")
                val copiesWon = scratchCardGame.getCopiesOfCardsWon(id, card.winningCount)
                    .also { it.map { l -> l.first }.println() }
                assertThat(copiesWon.map { it.first }).isEqualTo(expected[id - 1])
            }
        }
        println("elapsed no cache: ${elapsedNoCache/1000} microsecs")
        val elapsedWithCache = measureNanoTime {
            scratchCardGame.cards.forEach { (id, card) ->
                println("card $id")
                val copiesWon = scratchCardGame.getCopiesOfCardsWon(id, card.winningCount)
                    .also { it.map { l -> l.first }.println() }
                assertThat(copiesWon.map { it.first }).isEqualTo(expected[id - 1])
            }
        }
        println("elapsed with cache: ${elapsedWithCache/1000} microsecs")
        assertTrue(elapsedWithCache < elapsedNoCache)
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("30")
    }
}
