package mpdev.springboot.aoc2023.solutions.day05

import mpdev.springboot.aoc2023.solutions.day05.Almanac.State.*
import mpdev.springboot.aoc2023.utils.AocException

class Almanac(input: List<String>) {

    val seedsList = mutableListOf<Long>()
    val maps = mutableMapOf<State, MutableList<Pair<LongRange, Long>>>()

    init {
        processInput(input)
    }

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

    val seedRanges = mutableListOf<LongRange>()

    fun seedToRanges() {
        var i = 0
        while (i < seedsList.size) {
            val start = seedsList[i]
            val end = seedsList[i] + seedsList[i + 1]
            seedRanges.add(LongRange(start, end))
            i += 2
        }
    }

    fun getlocationFromRange(): List<Long> {
        var result = Long.MAX_VALUE
        seedRanges.forEach { r ->
            r.forEach { seed ->
                val l = getLocation(seed)
                //locations.add(l)
                if (l < result)
                    result = l
            }
        }
        return listOf(result)
    }

    fun getlocationRangeFromSeedRange(): List<LongRange> {
        val locations = mutableListOf<LongRange>()
        seedRanges.forEach { r ->
            locations.add(LongRange(getLocation(r.first), getLocation(r.last)))
        }
        return locations
    }

    fun findMinimumRange(rangeMap: List<Pair<LongRange, Long>>, refRanges: List<LongRange>, insideRange: Boolean): LongRange? {
        rangeMap.sortedBy { it.second }.reversed().forEach { range ->
            // val prevRange = LongRange()
        }
        return null
    }

    enum class State { Seeds, SeedToSoil, SoilToFertlzr, FertlzrToWater, WaterToLight, LightToTemp, TempToHumid, HumidToLctn }

    private fun processInput(input: List<String>) {
        var state = Seeds
        input.forEach { line ->
            try {
                if (line.isEmpty())
                    state = State.values()[state.ordinal + 1]
                else if (!line.contains("map"))
                    if (state == Seeds && line.startsWith("seeds")) {
                        seedsList.addAll(line.removePrefix("seeds:").trim().split(Regex(""" +""")).map { it.toLong() })
                    } else {
                        val (dest, src, len) = line.split(Regex(""" +""")).map { it.toLong() }
                        updateMap(state, dest, src, len)
                    }
            } catch (e: Exception) {
                throw AocException("bad input line $line")
            }
        }
    }

    private fun updateMap(state: State, dest: Long, src: Long, len: Long) {
        maps.getOrPut(state) { mutableListOf() }.add(Pair(LongRange(src, src + len - 1), dest - src))
    }
}
