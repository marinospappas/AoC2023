package mpdev.springboot.aoc2023.day01

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day01.Day01
import mpdev.springboot.aoc2023.solutions.day01.InputProcessor01
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class Day01Test {

    private val day = 1                                     ///////// Update this for a new dayN test
    private val inputProcessor = InputProcessor01()         ///////// Update this for a new dayN test
    private val puzzleSolver = Day01(inputProcessor)        ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private val inputLines: List<String> = inputDataReader.read(day)
    private var partResult = 0

    /*
    The first Elf is carrying food with 1000, 2000, and 3000 Calories, a total of 6000 Calories.
    The second Elf is carrying one food item with 4000 Calories.
    The third Elf is carrying food with 5000 and 6000 Calories, a total of 11000 Calories.
    The fourth Elf is carrying food with 7000, 8000, and 9000 Calories, a total of 24000 Calories.
    The fifth Elf is carrying one food item with 10000 Calories.
    */

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
    fun `Reads Input`() {
        val expected = listOf(
            "1000", "2000", "3000", "", "4000", "", "5000", "6000", "", "7000", "8000", "9000", "", "10000"
        )
        println("input data: $inputLines")
        assertThat(inputLines).isEqualTo(expected)
    }

    @Test
    @Order(3)
    fun `Calculates Calorie Sums List`() {
        val expected = listOf(
            listOf(1000,2000,3000), listOf(4000), listOf(5000,6000), listOf(7000,8000,9000), listOf(10000)
        )
        assertThat(inputProcessor.process(inputLines)).isEqualTo(expected)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val expected = 24000
        val elapsed = measureTimeMillis { partResult = puzzleSolver.solvePart1().result.toInt() }
        println("elapsed time part1: $elapsed  msec")
        assertThat(partResult).isEqualTo(expected)
    }

    @Test
    @Order(5)
    fun `Solves Part 2`() {
        val expected = 45000
        val elapsed = measureTimeMillis { partResult = puzzleSolver.solvePart2().result.toInt() }
        println("elapsed time part2: $elapsed  msec")
        assertThat(partResult).isEqualTo(expected) }
}
