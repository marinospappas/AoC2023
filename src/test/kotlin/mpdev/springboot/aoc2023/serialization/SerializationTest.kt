package mpdev.springboot.aoc2023.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mpdev.springboot.aoc2023.solutions.day02.Cube
import mpdev.springboot.aoc2023.utils.*
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

    @Test
    fun test2() {
        val input = listOf(
            "Card  1:41 48 83 86 17|83 86  6 31 17  9 48 53",
            "Card  2:  13 32 20 16 61 | 61 30 68 82 17 32 24 19",
            "Card 3:  1 21 53 59   44 | 69 82 63 72 16 21 14  1",
            "Card 4: 41 92 73  84 69 | 59 84 76 51 58  5 54 83",
            "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36",
            "Card 678901234567890: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"
        )
        //input.forEach { it.transformInput(Card::class.java).println() }
        //input.map { it.transformInput(Card::class.java).toJson(Card::class.java) }.forEach { it.println() }

        val cardsList = Json.decodeFromString<List<Card>>(
            input.joinToString(",", "[", "]") {  it.toJson(Card::class.java) }
        )
        cardsList.forEach { println(it) }
    }
}


@Serializable @InputClass("Card", [":", "\\|"])
data class Card(
    //Card  1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
    //      0  1                2
    @InputField(0) val id: Long,
    @InputField(1, " +") val winning: List<Int>,
    @InputField(2, " +") val numbers: List<String>
)

