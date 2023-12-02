package mpdev.springboot.aoc2023.solutions.day02

import mpdev.springboot.aoc2023.solutions.day02.Cube.*
import mpdev.springboot.aoc2023.utils.AocException

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
                try {
                    // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
                    val (id, remaining) = Regex("""Game (\d+): (.+)""").find(line)!!.destructured
                    games[id.toInt()] = processGame(remaining)
                } catch (e: Exception) {
                    throw AocException("bad input line [$line], ${e.message}")
                }
            }
            return games
        }

        private fun processGame(input: String): Set<CubeSet> {
            val setOfCubeSet = mutableSetOf<CubeSet>()
            input.split(Regex("; ")).forEach { g ->
                val cubeSet = CubeSet()
                g.split(Regex(", ")).forEach { c ->
                    val s = c.split(" ")
                    cubeSet.cubes.add(Pair(s[0].toInt(), Cube.valueOf(s[1])))
                }
                setOfCubeSet.add(cubeSet)
            }
            return setOfCubeSet
        }
    }
}

data class CubeSet(val cubes: MutableSet<Pair<Int,Cube>> = mutableSetOf())

enum class Cube { green, red, blue }