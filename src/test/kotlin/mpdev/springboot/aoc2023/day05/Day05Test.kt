package mpdev.springboot.aoc2023.day05

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day05.Almanac
import mpdev.springboot.aoc2023.solutions.day05.Day05
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day05Test {

    private val day = 5                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day05()                      ///////// Update this for a new dayN test
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
    fun `Reads Input ans sets Mappings`() {
        val almanac = Almanac(inputLines)
        println("seeds list")
        almanac.seedsList.println()
        Almanac.State.values().forEach { state ->
            println(state)
            almanac.maps[state]?.forEach { it.println() }
        }
    }

    @Test
    @Order(3)
    fun `Resolves Mappings for each Seed`() {
        val expected = listOf(
            listOf<Long>(81, 81, 81, 74, 78, 78, 82),
            listOf<Long>(14, 53, 49, 42, 42, 43, 43),
            listOf<Long>(57, 57, 53, 46, 82, 82, 86),
            listOf<Long>(13, 52, 41, 34, 34, 35, 35)
        )
        val almanac = Almanac(inputLines)
        almanac.seedsList.indices.forEach { indx ->
            val seed = almanac.seedsList[indx]
            print("seed $seed: ")
            var current = seed
            Almanac.State.values().forEach { state ->
                if (state != Almanac.State.Seeds) {
                    current = almanac.getMapping(state, current)
                    print("$state $current, ")
                    assertThat(current).isEqualTo(expected[indx][state.ordinal-1])
                }
            }
            println("")
        }
        assertThat(almanac.seedsList.minOf { almanac.getLocation(it) }.also { it.println() })
            .isEqualTo(35)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("35")
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("46")
    }
}
