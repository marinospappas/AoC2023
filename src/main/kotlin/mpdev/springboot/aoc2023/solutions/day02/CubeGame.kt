package mpdev.springboot.aoc2023.solutions.day02

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.solutions.day02.Cube.*
import mpdev.springboot.aoc2023.utils.ListType.*
import mpdev.springboot.aoc2023.utils.InputClass
import mpdev.springboot.aoc2023.utils.InputField
import mpdev.springboot.aoc2023.utils.InputUtils

class CubeGame(input: List<String>) {

    private var aocInputList: List<AoCInputDay02> = InputUtils(AoCInputDay02::class.java).readAoCInput(input)
    val games: Map<Int, Set<CubeSet>> = aocInputList.map { Pair(it.id, it.cubeList.map { cList -> CubeSet(cList.toSet()) }.toSet())  }
        .associate { it.first to it.second }
    val gameCubes = setOf(Pair(12, red), Pair(13, green), Pair(14, blue))

    fun isGameValid(game: Set<CubeSet>) =
        game.map { it.cubes }.flatten().all { cube -> cube.first <= gameCubes.first { it.second == cube.second }.first }

    fun minCubesForGame(game: Set<CubeSet>) =
        CubeSet(game.map { it.cubes }.flatten().groupBy { it.second }.values
            .map { Pair(it.maxOf { c -> c.first }, it.first().second) }.toSet() )

    fun powerOfSet(cubeSet: CubeSet) =
        cubeSet.cubes.map{ it.first }.reduce(Int::times)
}

@Serializable
@InputClass(delimiters = [":"], prefix = "Game")
data class AoCInputDay02(
    @InputField(0) val id: Int,
    @InputField(1, delimiters = [";", ",", " "], listType = [list, pair]) val cubeList: List<List<Pair<Int,Cube>>>
)

data class CubeSet(val cubes: Set<Pair<Int,Cube>> = setOf())

enum class Cube { green, red, blue }