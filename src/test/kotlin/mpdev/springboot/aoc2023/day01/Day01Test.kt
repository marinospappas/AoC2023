package mpdev.springboot.aoc2023.day01

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day01.Day01
import mpdev.springboot.aoc2023.utils.InputUtils
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day01Test {

    private val day = 1                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day01()                      ///////// Update this for a new dayN test
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
    fun `Reads Input ans sets Integer List`() {
        inputLines.forEach { InputUtils(Day01.AoCInputDay01::class.java).transform(it).println() }
        inputLines.forEach { InputUtils(Day01.AoCInputDay01::class.java).toJson(it).println() }
        puzzleSolver.inputStrings.forEach { it.println() }
        assertThat(puzzleSolver.inputStrings.size).isEqualTo(4)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("142")
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        puzzleSolver.inputData = listOf(
            "two1nine",
            "eightwothree",
            "abcone2threexyz",
            "xtwone3four",
            "4nineeightseven2",
            "zoneight234",
            "7pqrstsixteen"
        )
        puzzleSolver.initSolver()
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("281")
    }
}
