package mpdev.springboot.aoc2023.solutions.day02

import mpdev.springboot.aoc2023.solutions.day02.Cube.*
import mpdev.springboot.aoc2023.utils.AocException

class CubeGame(input: List<String>) {

    val games = mutableMapOf<Int, MutableSet<CubeSet>>()
    val gameCubes = setOf(Pair(12, red), Pair(13, green), Pair(14, blue))

    init {
        processInput(input, games)
    }

    fun isGameValid(game: Set<CubeSet>) =
        game.map { it.cubes }.flatten().all { cube -> cube.first <= gameCubes.first { it.second == cube.second }.first }

    fun minCubesForGame(game: Set<CubeSet>) =
        CubeSet(
            Cube.values()
                .associateWith { c -> game.map { it.cubes }.flatten().filter { it.second == c }.maxOf { it.first } }
                .entries.map { e -> Pair(e.value, e.key) }.toMutableSet()
        )

    fun powerOfSet(cubeSet: CubeSet) =
        cubeSet.cubes.map{ it.first }.fold(1) { acc, x -> acc * x }


    companion object {
        private fun processInput(input: List<String>, games: MutableMap<Int, MutableSet<CubeSet>>) {
            input.forEach { line ->
                // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
                val match = Regex("""Game (\d+): (.+)""").find(line)
                try {
                    val (id, remaining) = match!!.destructured
                    games[id.toInt()] = mutableSetOf()
                    processGame(id.toInt(), remaining, games)
                } catch (e: Exception) {
                    throw AocException("bad input line [$line], ${e.message}")
                }
            }
        }

        private fun processGame(id: Int, input: String, games: MutableMap<Int, MutableSet<CubeSet>>) {
            input.split(Regex("; ")).forEach { g ->
                // 1 red, 2 green, 6 blue
                val cubeSet = CubeSet()
                g.split(Regex(", ")).forEach { c ->
                    val match = Regex("""(\d+) ([a-z]+)""").find(c)
                    try {
                        val (count, cube) = match!!.destructured
                        cubeSet.cubes.add(Pair(count.toInt(), Cube.valueOf(cube)))
                    } catch (e: Exception) {
                        throw AocException("bad input for cubes $input")
                    }
                }
                games[id]!!.add(cubeSet)
            }
        }
    }
}

data class CubeSet(val cubes: MutableSet<Pair<Int,Cube>> = mutableSetOf())

enum class Cube { green, red, blue }