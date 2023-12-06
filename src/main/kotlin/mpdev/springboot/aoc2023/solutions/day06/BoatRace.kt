package mpdev.springboot.aoc2023.solutions.day06

import mpdev.springboot.aoc2023.utils.QuadraticEq
import mpdev.springboot.aoc2023.utils.pairWith
import kotlin.math.ceil
import kotlin.math.floor

class BoatRace(input: List<String>) {

    val races = processInput(input).map { Race(it.first, it.second) }

    init {
        processInput(input)
    }

    fun minMaxChargeTime(race: Race): Pair<Long,Long> {
        val a = 1L
        val b = -race.time
        val c = race.distance + 1
        val (x1, x2) = QuadraticEq.solve(a, b, c)
        return Pair(ceil(x1).toLong(), floor(x2).toLong())
    }

    fun setupLongRace() = races.reduce(Race::concat)

    private fun processInput(input: List<String>): List<Pair<Long,Long>> {
        val times = input[0].removePrefix("Time:").trim().split(Regex(" +")).map { it.trim().toLong() }
        val distances = input[1].removePrefix("Distance:").trim().split(Regex(" +")).map { it.trim().toLong() }
        return times.pairWith(distances)
    }
}

data class Race(val time: Long, val distance: Long) {
    fun concat(other: Race) = Race(
        "${this.time}${other.time}".toLong(), "${this.distance}${other.distance}".toLong()
    )
}
