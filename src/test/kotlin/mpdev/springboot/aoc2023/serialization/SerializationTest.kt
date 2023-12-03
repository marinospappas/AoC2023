package mpdev.springboot.aoc2023.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mpdev.springboot.aoc2023.solutions.day02.Cube
import mpdev.springboot.aoc2023.utils.println
import org.junit.jupiter.api.Test

class SerializationTest {

    @Serializable
    data class CubeGame(val id: Int, val cubeSet: Set<CubeSet>)

    @Serializable
    data class CubeSet(val cubes: Set<Pair<Int, Cube>> = setOf())

    @Test
    fun test1() {
        val cubeSet = CubeSet(setOf(Pair(3,Cube.blue), Pair(4,Cube.red)))
        val cubeSet1 = CubeSet(setOf(Pair(1,Cube.red), Pair(2,Cube.green), Pair(6,Cube.blue)))
        val cubeSet2 = CubeSet(setOf(Pair(2,Cube.green)))
        val cubeSetJson = Json.encodeToString(cubeSet).println()
        val cubeGame = CubeGame(1, setOf(cubeSet, cubeSet1, cubeSet2))
        val cubeGameJson = Json.encodeToString(cubeGame).println()
        // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        val input = "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
        val jsonInput = input.replace("Game ", """{"id":""")
            .replace(": ", ""","cubeSet":[{"cubes":[{"first":""")
            .replace(Regex("""$"""), """"}]}]}""")
            .replace(Regex(", "), """"},{"first":""")
            .replace(Regex("; "), """"}]},{"cubes":[{"first":""")
            .replace(Regex(" "), ""","second":"""")
            .also { it.println() }
        val game: CubeGame = Json.decodeFromString<CubeGame>(jsonInput).also { it.println() }
        val gameMap = mapOf(1 to setOf(cubeSet, cubeSet1, cubeSet2))
        val map1Json = Json.encodeToString(gameMap).println()
        val jsonInput1 = input.replace("Game ", """{""")
            .replace(": ", """:[{"cubes":[{"first":""")
            .replace(Regex("""$"""), """"}]}]}""")
            .replace(Regex(", "), """"},{"first":""")
            .replace(Regex("; "), """"}]},{"cubes":[{"first":""")
            .replace(Regex(" "), ""","second":"""")
            .also { it.println() }
        val mapFromInput: Map<Int,Set<CubeSet>> = Json.decodeFromString<Map<Int,Set<CubeSet>>>(jsonInput1).also { it.println() }

        val input1 = listOf(
            "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
            "Game 2: 6 blue, 12 red; 4 red, 12 green, 3 blue"
        )
        val cubeSet3 = CubeSet(setOf(Pair(6,Cube.blue), Pair(12,Cube.red)))
        val cubeSet4 = CubeSet(setOf(Pair(4,Cube.red), Pair(12,Cube.green), Pair(3,Cube.blue)))
        val gameMap1 = mapOf(1 to setOf(cubeSet, cubeSet1, cubeSet2),
            2 to setOf(cubeSet3, cubeSet4))
        val map1Json1 = Json.encodeToString(gameMap1).println()
        val inputJason2 = input1.map { it
            .replace("Game ", """""")
            .replace(": ", """:[{"cubes":[{"first":""")
            .replace(Regex("""$"""), """"}]}]""")
            .replace(Regex(", "), """"},{"first":""")
            .replace(Regex("; "), """"}]},{"cubes":[{"first":""")
            .replace(Regex(" "), ""","second":"""")
        }.joinToString(",", "{", "}").also { it.println() }
        val mapFromInput1: Map<Int,Set<CubeSet>> = Json.decodeFromString<Map<Int,Set<CubeSet>>>(inputJason2).also { it.println() }

    }
}