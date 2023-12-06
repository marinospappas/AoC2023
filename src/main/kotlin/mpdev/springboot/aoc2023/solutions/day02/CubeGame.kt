package mpdev.springboot.aoc2023.solutions.day02

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mpdev.springboot.aoc2023.solutions.day02.Cube.*

class CubeGame(input: List<String>) {

    val games = Json.decodeFromString<Map<Int, Set<CubeSet>>>(
        input.joinToString(",", "{", "}") { it.toJson() }
    )
    val gameCubes = setOf(Pair(12, red), Pair(13, green), Pair(14, blue))

    fun isGameValid(game: Set<CubeSet>) =
        game.map { it.cubes }.flatten().all { cube -> cube.first <= gameCubes.first { it.second == cube.second }.first }

    fun minCubesForGame(game: Set<CubeSet>) =
        CubeSet(game.map { it.cubes }.flatten().groupBy { it.second }.values
            .map { Pair(it.maxOf { c -> c.first }, it.first().second) }.toSet() )

    fun powerOfSet(cubeSet: CubeSet) =
        cubeSet.cubes.map{ it.first }.reduce(Int::times)

    companion object {
        fun String.toJson() =
            // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            // 1:[{"cubes":[{"first":3,"second":"blue"},{"first":4,"second":"red"}]},{"cubes":[{"first":1,"second":"red"},
            // {"first":2,"second":"green"},{"first":6,"second":"blue"}]},{"cubes":[{"first":2,"second":"green"}]}]
            this.replace("Game ", """""")
                .replace(": ", """:[{"cubes":[{"first":""")
                .replace(Regex("""$"""), """"}]}]""")
                .replace(Regex(", "), """"},{"first":""")
                .replace(Regex("; "), """"}]},{"cubes":[{"first":""")
                .replace(Regex(" "), ""","second":"""")
    }
}

@Serializable
data class CubeSet(val cubes: Set<Pair<Int,Cube>> = setOf())

enum class Cube { green, red, blue }