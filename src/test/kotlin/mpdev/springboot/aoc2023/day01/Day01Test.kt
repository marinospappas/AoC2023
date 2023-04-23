package mpdev.springboot.aoc2023.day01

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day01.Day01
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day01Test {

    private val day = 1
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private val inputLines: List<String> = inputDataReader.read(day)
    private val puzzleSolver = Day01()

    /*
    The first Elf is carrying food with 1000, 2000, and 3000 Calories, a total of 6000 Calories.
    The second Elf is carrying one food item with 4000 Calories.
    The third Elf is carrying food with 5000 and 6000 Calories, a total of 11000 Calories.
    The fourth Elf is carrying food with 7000, 8000, and 9000 Calories, a total of 24000 Calories.
    The fifth Elf is carrying one food item with 10000 Calories.
    */

    @Test
    @Order(1)
    fun `Sets day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input`() {
        val expected = listOf(
            "1000", "2000", "3000", "", "4000", "", "5000", "6000", "", "7000", "8000", "9000", "", "10000"
        )
        println(inputLines)
        assertThat(inputLines).isEqualTo(expected)
    }

    @Test
    @Order(3)
    fun `Calculates Calorie Sums`() {
        val expected = listOf(
            listOf(1000,2000,3000), listOf(4000), listOf(5000,6000), listOf(7000,8000,9000), listOf(10000)
        )
        assertThat(puzzleSolver.processInput(inputLines)).isEqualTo(expected)
    }

    @Test
    @Order(4)
    fun `Calculates Part 1`() {
        val expected = 24000
        puzzleSolver.inputData = inputLines
        val result = puzzleSolver.part1().result.toInt()
        assertThat(result).isEqualTo(expected)
    }

    @Test
    @Order(5)
    fun `Calculates Part 2`() {
        val expected = 45000
        puzzleSolver.inputData = inputLines
        val result = puzzleSolver.part2().result.toInt()
        assertThat(result).isEqualTo(expected) }
}
