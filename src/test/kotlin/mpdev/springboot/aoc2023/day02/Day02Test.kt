package mpdev.springboot.aoc2023.day02

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day02.*
import mpdev.springboot.aoc2023.utils.InputUtils
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day02Test {

    private val day = 2                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day02()                      ///////// Update this for a new dayN test
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
    fun `Reads Input ans sets Games List`() {
        val cubeGame = CubeGame(inputLines)
        println("input transformed")
        inputLines.forEach { InputUtils(AoCInput::class.java).transform(it).println() }
        println("input to json")
        inputLines.map { InputUtils(AoCInput::class.java).transform(it) }
            .filterNot { InputUtils.skipEmptyLines && it.isEmpty() }
            .joinToString(",", "[", "]") { InputUtils(AoCInput::class.java).toJson(it) }
            .println()
        println(cubeGame.gameCubes)
        cubeGame.games.forEach { it.println() }
        assertThat(cubeGame.games.size).isEqualTo(5)
        assertThat(cubeGame.gameCubes).isEqualTo(setOf(
            Pair(12, Cube.red), Pair(13, Cube.green), Pair(14, Cube.blue)
        ))
    }

    @Test
    @Order(3)
    fun `Identifies Valid Games`() {
        val cubeGame = CubeGame(inputLines)
        val validGames = cubeGame.games.filter { cubeGame.isGameValid(it.value) }
        validGames.forEach { it.println() }
        val result = cubeGame.games.entries.filter { e -> cubeGame.isGameValid(e.value) }
            .sumOf { e -> e.key }
        println("sum of ids: $result")
        assertThat(validGames.keys).isEqualTo(setOf(1,2,5))
        assertThat(result).isEqualTo(8)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("8")
    }

    @Test
    @Order(5)
    fun `Calculates Minimum Cubes Set and Power of Set of Cubes`() {
        val expected = listOf(48, 12, 1560, 630, 36)
        val cubeGame = CubeGame(inputLines)
        cubeGame.games.entries
            .forEach { e ->
                println("id ${e.key}: min cubes ${cubeGame.minCubesForGame(e.value)}")
                val power = cubeGame.powerOfSet(cubeGame.minCubesForGame(e.value))
                println("pwr: $power")
                assertThat(power).isEqualTo(expected[e.key - 1])
            }
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("2286")
    }
}
