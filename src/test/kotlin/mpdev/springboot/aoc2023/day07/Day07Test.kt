package mpdev.springboot.aoc2023.day07

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day07.*
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

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
    @Order(3)
    fun `Reads Input ans sets Hands List`() {
        val camelCards = CamelCards(inputLines)
        camelCards.handsList.forEach { it.println() }
        assertThat(camelCards.handsList.size).isEqualTo(5)
        assertThat(camelCards.handsList.sumOf { it.bid }).isEqualTo(2180)
    }

    @Test
    @Order(3)
    fun `Identifies Type of each Hand`() {
        val camelCards = CamelCards(inputLines)
        val expected = listOf(HandType.OnePair, HandType.ThreeOfaKind, HandType.TwoPairs, HandType.TwoPairs, HandType.ThreeOfaKind)
        camelCards.handsList.indices.forEach { i ->
            val type = HandType.getType(camelCards.handsList[i])
            println("${camelCards.handsList[i].cards}  $type")
            assertThat(type).isEqualTo(expected[i])
        }
    }

    @Test
    @Order(4)
    fun `Sorts Hand - no Joker`() {
        val camelCards = CamelCards(inputLines)
        val expected = listOf(765, 220, 28, 684, 483)
        val sorted = camelCards.handsList.sortedWith(HandComparator(false)).onEach { h -> h.println() }
        assertThat(sorted.map { it.bid }).isEqualTo(expected)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("6440")
    }

    @ParameterizedTest
    @CsvSource(value = [
        // input values
        "32T3K, OnePair", "T55J5, FourOfaKind", "KK677, TwoPairs", "KTJJT, FourOfaKind", "QQQJA, FourOfaKind",
        // 4 or 5 Jockers
        "JJJJJ, FiveOfaKind", "JJ2JJ, FiveOfaKind",
        // 3 Jokers
        "3JJ3J, FiveOfaKind", "JJ24J, FourOfaKind",
        // 2 Jokers
        "JJ345, ThreeOfaKind", "JJ334, FourOfaKind", "JJ333, FiveOfaKind",
        // 1 Joker
        "JQQQQ, FiveOfaKind", "JAQQQ, FourOfaKind", "JAAQQ, FullHouse", "JA3QQ, ThreeOfaKind", "JA234, OnePair",
    ])
    @Order(6)
    fun `Identifies Type of each Hand with Joker`(cards: String, expected: HandType) {
        val type = HandType.getType(Hand(cards), true)
        println("$cards  $type")
        assertThat(type).isEqualTo(expected)
    }

    @Test
    @Order(7)
    fun `Sorts Hand - with Joker`() {
        val camelCards = CamelCards(inputLines)
        val expected = listOf(765, 28, 684, 483, 220)
        val sorted = camelCards.handsList.sortedWith(HandComparator(true)).onEach { h -> h.println() }
        assertThat(sorted.map { it.bid }).isEqualTo(expected)
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("5905")
    }

    @Test
    fun testx() {
        HandType.getType(Hand("1233J"), true).println()


        HandType.getType(Hand("JJAK2"), true).println()
        HandType.getType(Hand("JJAA2"), true).println()

        HandType.getType(Hand("J2JJK"), true).println()
        HandType.getType(Hand("JAJJK"), true).println()

        HandType.getType(Hand("JJ2JJ"), true).println()
        HandType.getType(Hand("J2JJJ"), true).println()
        HandType.getType(Hand("JJ2JJ"), true).println()
        HandType.getType(Hand("QJJJT"), true).println()
        HandType.getType(Hand("JJJJJ"), true).println()
        HandType.getType(Hand("AKKJJ"), true).println()
        HandType.getType(Hand("JJJJ2"), true).println()
        HandComparator().compare(Hand("KK677"), Hand("T55J5")).println()
    }
}
