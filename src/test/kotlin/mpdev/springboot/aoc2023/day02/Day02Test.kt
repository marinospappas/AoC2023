package mpdev.springboot.aoc2023.day02

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day02.Cube
import mpdev.springboot.aoc2023.solutions.day02.CubeGame
import mpdev.springboot.aoc2023.solutions.day02.CubeGame.Companion.toJson
import mpdev.springboot.aoc2023.solutions.day02.CubeSet
import mpdev.springboot.aoc2023.solutions.day02.Day02
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
    fun `Deserializes Input`() {
        // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        val input = "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
        val cubeSet0 = CubeSet(setOf(Pair(3, Cube.blue), Pair(4, Cube.red)))
        val cubeSet1 = CubeSet(setOf(Pair(1, Cube.red), Pair(2, Cube.green), Pair(6, Cube.blue)))
        val cubeSet2 = CubeSet(setOf(Pair(2, Cube.green)))
        val gameMap = mapOf(1 to setOf(cubeSet0, cubeSet1, cubeSet2))
        Json.encodeToString(gameMap).also { it.println() }
        // read input
        val game = Json.decodeFromString<Map<Int, Set<CubeSet>>>(
            listOf(input).joinToString(",", "{", "}") { it.toJson() }.also { it.println() }
        ).also { it.println() }
        assertThat(game.keys.first()).isEqualTo(1)
        assertThat(game.size).isEqualTo(1)
    }

    @Test
    @Order(2)
    fun `Reads Input ans sets Games List`() {
        val cubeGame = CubeGame(inputLines)
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
