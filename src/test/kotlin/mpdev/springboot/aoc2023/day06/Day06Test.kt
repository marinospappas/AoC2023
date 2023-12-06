package mpdev.springboot.aoc2023.day06

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day06.Day06
import mpdev.springboot.aoc2023.solutions.day06.BoatRace
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day06Test {

    private val TEST = "test"

    private val day = 6                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day06()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/$TEST/resources/inputdata/input")
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
    fun `Reads Input ans sets Races`() {
        val boatRace = BoatRace(inputLines)
        boatRace.races.forEach { it.println() }
        assertThat(boatRace.races.size).isEqualTo(3)
    }

    @Test
    @Order(3)
    fun `Solves Quadratic Equation`() {
        val boatRace = BoatRace(inputLines)
        val expected = listOf(
            Pair(2L, 5L), Pair(4L, 11L), Pair(11L, 19L)
        )
        boatRace.races.indices.forEach { i ->
            val minMax = boatRace.minMaxChargeTime(boatRace.races[i]).also { it.println() }
            assertThat(minMax).isEqualTo(expected[i])
        }
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("288")
    }

    @Test
    @Order(6)
    fun `Sets up Long Race`() {
        val boatRace = BoatRace(inputLines)
        val longRace = boatRace.setupLongRace().also { it.println() }
        assertThat(longRace.time).isEqualTo(71530)
        assertThat(longRace.distance).isEqualTo(940200)
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("71503")
    }
}
