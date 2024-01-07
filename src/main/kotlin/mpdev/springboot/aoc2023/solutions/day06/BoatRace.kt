package mpdev.springboot.aoc2023.solutions.day06

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.*
import kotlin.math.ceil
import kotlin.math.floor

@Serializable
@AocInClass(delimiters = ["Distance: *"])
@AocInRemovePatterns(["Time: *"])
data class AoCInput(
    // Time:      7  15   30
    // Distance:  9  40  200
    @AocInField(0, delimiters = [" +"]) val times: List<Long>,
    @AocInField(1, delimiters = [" +"]) val distances: List<Long>
)

class BoatRace(input: List<String>) {

    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(listOf(input.joinToString("" )))
    val races = (0 .. aocInputList[0].times.lastIndex).map {
        Race(aocInputList[0].times[it], aocInputList[0].distances[it])
    }

    fun minMaxChargeTime(race: Race): Pair<Long,Long> {
        val a = 1L
        val b = -race.time
        val c = race.distance + 1
        val (x1, x2) = QuadraticEq.solve(a, b, c)
        return Pair(ceil(x1).toLong(), floor(x2).toLong())
    }

    fun setupLongRace() = races.reduce(Race::concat)
}

data class Race(val time: Long, val distance: Long) {
    fun concat(other: Race) = Race(
        "${this.time}${other.time}".toLong(), "${this.distance}${other.distance}".toLong()
    )
}
