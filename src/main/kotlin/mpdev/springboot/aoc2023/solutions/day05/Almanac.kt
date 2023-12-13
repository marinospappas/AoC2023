package mpdev.springboot.aoc2023.solutions.day05

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.solutions.day05.Almanac.State.*
import mpdev.springboot.aoc2023.utils.*
import kotlin.math.max
import kotlin.math.min

@Serializable
@AocInClass(delimiters = [","])
@AocInRetainValues(patterns = ["""^(.+) map:"""])
@AocInReplacePatterns(patterns = ["seeds:", """^[^s]"""], replace = ["seeds,", "$1,"])
@AocInRemovePatterns(patterns = ["""^.+ map:"""])
data class AoCInput(
    // seeds: 79 14 55 13
    // 0      1
    @AocInField(0) val mapId: String,
    @AocInField(1, delimiters = [" "]) val values: List<Long>
)

class Almanac(input: List<String>) {

    //private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    val seedsList = mutableListOf<Long>()
    val maps = mutableMapOf<State, MutableList<Pair<LongRange, Long>>>()

    init {
        processInput(input)
    }

    // part 1

    fun getLocation(seed: Long): Long {
        var current = seed
        State.values().forEach { state ->
            current = if (state == Seeds) seed
            else getMapping(state, current)
        }
        return current
    }

    fun getMapping(state: State, input: Long): Long {
        maps[state]?.forEach {
            if (input in it.first)
                return input + it.second
        }
        return input
    }

    // part 2

    private val seedRanges = mutableListOf<LongRange>()

    fun seedToRanges() {
        var i = 0
        while (i < seedsList.size) {
            val start = seedsList[i]
            val end = seedsList[i] + seedsList[i + 1]
            seedRanges.add(LongRange(start, end))
            i += 2
        }
    }

    fun getMinLocationFromRange(): Long {
        var result = Long.MAX_VALUE
        seedRanges.forEach { r ->
            r.forEach { seed ->
                getLocation(seed).let { if (it < result) result = it }
            }
        }
        return result
    }

    // experimental to try part 2 with no brute-force
    fun findMinimumRange(rangeMap: List<Pair<LongRange, Long>>, refRanges: List<LongRange>, insideRange: Boolean): LongRange? {
        rangeMap.sortedBy { it.second }.reversed().forEach { range ->
            val start = range.first.first - range.second
            val end = range.first.last - range.second
            refRanges.forEach { refRange ->
                if (insideRange) {
                    if (start in refRange)
                        return LongRange(max(start, refRange.first), min(end, refRange.last))
                }
                else {
                    if (start !in refRange)
                        return LongRange(start, end)
                }
            }
        }
        return null
    }

    enum class State { Seeds, SeedToSoil, SoilToFertlzr, FertlzrToWater, WaterToLight, LightToTemp, TempToHumid, HumidToLctn }

    private fun processInput(input: List<String>) {
        var state = Seeds
        input.forEach { line ->
            if (line.isEmpty())
                    state = State.values()[state.ordinal + 1]
            else if (!line.contains("map"))
                if (state == Seeds && line.startsWith("seeds"))
                        seedsList.addAll(line.removePrefix("seeds:").trim().split(Regex(""" +""")).map { it.toLong() })
                else {
                    val (dest, src, len) = line.split(Regex(""" +""")).map { it.toLong() }
                    updateMap(state, dest, src, len)
                }
        }
    }
    private fun updateMap(state: State, dest: Long, src: Long, len: Long) {
        maps.getOrPut(state) { mutableListOf() }.add(Pair(LongRange(src, src + len - 1), dest - src))
    }
}
