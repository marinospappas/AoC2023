package mpdev.springboot.aoc2023.solutions.day02

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mpdev.springboot.aoc2023.solutions.day02.Cube.*

class CubeGame(input: List<String>) {

    val games: Map<Int,Set<CubeSet>>
    val gameCubes = setOf(Pair(12, red), Pair(13, green), Pair(14, blue))

    init {
        games = processInput(input)
    }

    fun isGameValid(game: Set<CubeSet>) =
        game.map { it.cubes }.flatten().all { cube -> cube.first <= gameCubes.first { it.second == cube.second }.first }

    fun minCubesForGame(game: Set<CubeSet>) =
        CubeSet(game.map { it.cubes }.flatten().groupBy { it.second }.values
            .map { Pair(it.maxOf { c -> c.first }, it.first().second) }.toMutableSet() )

    fun powerOfSet(cubeSet: CubeSet) =
        cubeSet.cubes.map{ it.first }.fold(1) { acc, x -> acc * x }


    companion object {
        private fun processInput(input: List<String>): Map<Int,Set<CubeSet>> {
            val games = mutableMapOf<Int,Set<CubeSet>>()
            input.forEach { line ->
                // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
                // {1:[{"cubes":[{"first":3,"second":"blue"},{"first":4,"second":"red"}]},{"cubes":[{"first":1,"second":"red"},
                // {"first":2,"second":"green"},{"first":6,"second":"blue"}]},{"cubes":[{"first":2,"second":"green"}]}]}
                val inputJson = line.replace("Game ", """{""")
                    .replace(": ", """:[{"cubes":[{"first":""")
                    .replace(Regex("""$"""), """"}]}]}""")
                    .replace(Regex(", "), """"},{"first":""")
                    .replace(Regex("; "), """"}]},{"cubes":[{"first":""")
                    .replace(Regex(" "), ""","second":"""")
                Json.decodeFromString<Map<Int,Set<CubeSet>>>(inputJson).also {
                    games[it.entries.first().key] = it.entries.first().value
                }
            }
            return games
        }
    }
}

@Serializable
data class CubeSet(val cubes: Set<Pair<Int,Cube>> = setOf())

enum class Cube { green, red, blue }