package mpdev.springboot.aoc2023.solutions.day02

import mpdev.springboot.aoc2023.solutions.day02.Cube.*
import mpdev.springboot.aoc2023.utils.AocException

class CubeGame(input: List<String>) {

    val games = mutableMapOf<Int, MutableSet<Pair<Int, Cube>>>()
    val cubesSet = setOf(
        Pair(12, red), Pair(13, green), Pair(14, blue)
    )

    init {
        processInput(input)
    }

    fun isGameValid(game: Set<Pair<Int, Cube>>) =
        game.all { cube -> cube.first <= cubesSet.first { it.second == cube.second }.first }

    fun minCubesForGame(game: Set<Pair<Int, Cube>>) =
        Cube.values().associateWith { c -> game.filter { it.second == c }.maxOf { it.first } }
            .entries.map { e -> Pair(e.value, e.key) }.toSet()

    fun powerOfSet(cubes: Set<Pair<Int, Cube>>) =
        cubes.map{ it.first }.fold(1) { acc, x -> acc * x }

    private fun processInput(input: List<String>) {
        input.forEach { line ->
            // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            val match = Regex("""Game (\d+): (.+)""").find(line)
            try {
                val (id, remaining) = match!!.destructured
                processGame(id.toInt(), remaining)
            } catch (e: Exception) {
                throw AocException("bad input line [$line], ${e.message}")
            }
        }
    }

    private fun processGame(id: Int, input: String) {
        input.split(Regex("; ")).forEach { g ->
            // 1 red, 2 green, 6 blue
            g.split(Regex(", ")).forEach { c ->
                val match = Regex("""(\d+) ([a-z]+)""").find(c)
                try {
                    val (count, cube) = match!!.destructured
                    games.getOrPut(id) { mutableSetOf() }.add(Pair(count.toInt(), Cube.valueOf(cube)))
                } catch (e: Exception) {
                    throw AocException("bad input for cubes $input")
                }
            }
        }
    }
}

enum class Cube { green, red, blue }