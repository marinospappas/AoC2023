package mpdev.springboot.aoc2023.serialization

import kotlinx.serialization.Serializable
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
        val cubeSet = CubeSet(setOf(Pair(1,Cube.blue), Pair(2,Cube.red), Pair(3,Cube.green)))
        val cubeSet1 = CubeSet(setOf(Pair(10,Cube.blue), Pair(20,Cube.red), Pair(30,Cube.green)))
        val cubeSetJson = Json.encodeToString(cubeSet).println()
        val cubeGame = CubeGame(15, setOf(cubeSet, cubeSet1))
        val cubeGameJson = Json.encodeToString(cubeGame).println()
    }
}