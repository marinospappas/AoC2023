package mpdev.springboot.aoc2023.solutions.day05

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Serializable
@AocInClass(delimiters = [","])
@AocInRetainValues(patterns = ["""^(.+) map:"""])
@AocInReplacePatterns(patterns = ["""^""", "$1,"])
@AocInRemovePatterns(patterns = ["""^.+ map:"""])
data class AoCInput(
    // seeds: 79 14 55 13
    // 0      1
    @AocInField(0) val mapId: String,
    @AocInField(1, delimiters = [" "]) val values: List<Long>
)

class Almanac(input: List<String>) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    private val aocInputList: List<AoCInput> =
        InputUtils(AoCInput::class.java).readAoCInput(input.subList(1, input.size))
    val seedsList: List<Long> = input[0].removePrefix("seeds: ").split(" ").map { it.toLong() }
    val maps: Map<MapId, List<Pair<LongRange, Long>>>

    init {
        val tempMaps: MutableMap<MapId, MutableList<Pair<LongRange, Long>>> = mutableMapOf()
        aocInputList.forEach { e ->
            tempMaps.getOrPut(MapId.of(e.mapId)) { mutableListOf() }
                .add(Pair(LongRange(e.values[1], e.values[1] + e.values[2] - 1), e.values[0] - e.values[1]))
        }
        // the ranges in each map are not overlapping (the start of each range is > than the end of the previous range)
        maps = tempMaps.entries.associate { e -> e.key to e.value.sortedBy { it.first.first } }
    }

    // part 1

    fun getLocation(seed: Long): Long {
        // pass a seed value repeatedly through all the mapping layers
        var current = seed
        MapId.values().forEach { map -> current = getMapping(map, current) }
        return current
    }

    fun getMapping(mapId: MapId, input: Long): Long {
        // if within one of the maps, then apply transformation, else leave as is
        maps[mapId]?.forEach {
            if (input in it.first)
                return input + it.second
        }
        return input
    }

    // part 2

    val seedRanges: List<LongRange> = seedsList.indices.mapNotNull {
        if (it % 2 == 0) LongRange(seedsList[it], seedsList[it] + seedsList[it + 1]) else null
    }.combineRanges()

    // initial brute force method - kept for testing only
    fun getMinLocationFromRange(): Long {
        var result = Long.MAX_VALUE
        seedRanges.forEach { r ->
            r.forEach { seed ->
                getLocation(seed).let { if (it < result) result = it }
            }
        }
        return result
    }

    fun transformRangesThroughAllMaps(): List<LongRange> {
        var current = seedRanges.toList()
        log.info("part 2: starting ranges {}", current)
        maps.forEach { (key, map) ->
            current = transformRangeList(current, map).combineRanges()
            log.info("processed Map {} new ranges {}", key, current)
        }
        return current
    }

    fun transformRangeList(ranges: List<LongRange>, xformMap: List<Pair<LongRange, Long>>): List<LongRange> {
        val newRanges = mutableListOf<LongRange>()
        for (range in ranges)
            newRanges.addAll(transformRange(range, xformMap))
        return newRanges.sortedBy { it.first }
    }

    // combines overlapping and/or continuous ranges
    private fun List<LongRange>.combineRanges(): List<LongRange> {
        var current = this.toList()
        do {
            var combineMore = false
            val tmpRange = current.toMutableList()
            outerFor@ for (range in current) {
                for (other in mutableListOf<LongRange>().also { it.addAll(current) }.also { it.remove(range) }) {
                    if (range.last in other || range.last + 1L == other.first) {
                        val combinedRange = LongRange(
                            kotlin.math.min(range.first, other.first), kotlin.math.max(range.last, other.last)
                        )
                        tmpRange.remove(range)
                        tmpRange.remove(other)
                        tmpRange.add(combinedRange)
                        combineMore = true
                        break@outerFor
                    }
                }
            }
            current = tmpRange.toList()
        } while (combineMore)
        return current.sortedBy { it.first }
    }

    // transforms a range as per the List of Range / transform factor
    // List: .......X-----X.......X-------X.......X----------X.........
    // Start:  s--------                                                   before the beginning
    //         |       s-------                                            inside a range
    //         |       |       s--------                                   between ranges
    //         |       |       |                                  s----e   outside the end (also with end
    //         |       |       |
    // End:    s--e    |       |                                           before the beginning (also with start)
    //                 s--e    |                                           inside a range (also with start)
    //                         s---e                                       between ranges (also with start)
    //                            ----e
    //                                 --------e
    //                                                      -------e
    fun transformRange(range: LongRange, xformMap: List<Pair<LongRange, Long>>): List<LongRange> {
        val xformRanges = xformMap.map { it.first }
        var startEnd = 0
        val result = mutableListOf<LongRange>()
        for (i in xformRanges.indices) {
            if (startEnd == 0) { // looking for the start
                @Suppress("UNCHECKED_CAST")   // checkStartForRange will return either Int or List<LongRange>
                when (val startResult = checkStartOfRange(range, xformMap, i, result)) {
                    is List<*> -> return startResult as List<LongRange>
                    -1 -> break
                    0 -> continue
                    1 -> startEnd = 1
                }
            }
            else {  // looking for the end
                when (checkEndOfRange(range, xformMap, i, result)) {
                    -1 -> break
                    0 -> continue
                }
            }
        }
        // check if the input range extends beyond the end of the mapping ranges
        if (range.last > xformRanges.last().last)
            if (range.first > xformRanges.last().last)
                return listOf(range)
            else
                result.add(LongRange(xformRanges.last().last + 1, range.last))
        return result.sortedBy { it.first }
    }

    private fun checkStartOfRange(range: LongRange, xformMap: List<Pair<LongRange, Long>>, index: Int, resultRanges: MutableList<LongRange>)
    : Any {
        val xformRanges = xformMap.map { it.first }
        val factors = xformMap.map { it.second }
        when {
            range.first < xformRanges[index].first -> {
                if (range.last < xformRanges[index].first)
                    return listOf(range)
                else {
                    resultRanges.add(LongRange(range.first, xformRanges[index].first - 1))
                    // in this scenario need to also check if the end is within the xform range
                    if (range.last <= xformRanges[index].last) {
                        resultRanges.add(LongRange(xformRanges[index].first + factors[index], range.last + factors[index]))
                        return -1   //  break the loop
                    } else
                        resultRanges.add(LongRange(xformRanges[index].first + factors[index], xformRanges[index].last + factors[index]))
                }
                return 1   // go on and check the end of the range
            }
            range.first < xformRanges[index].last -> {
                if (range.last <= xformRanges[index].last)
                    return listOf(LongRange(range.first + factors[index], range.last + factors[index]))
                else
                    resultRanges.add(LongRange(range.first + factors[index], xformRanges[index].last + factors[index]))
                return 1   // go on and check the end of the range
            }
        }
        return 0   // continue the loop
    }

    private fun checkEndOfRange(range: LongRange, xformMap: List<Pair<LongRange,Long>>, index: Int, resultRanges: MutableList<LongRange>)
    : Int {
        val xformRanges = xformMap.map { it.first }
        val factors = xformMap.map { it.second }
        when {
            range.last < xformRanges[index].first -> {
                resultRanges.add(LongRange(xformRanges[index - 1].last + 1, range.last))
                return -1   // break the loop
            }
            range.last <= xformRanges[index].last -> {
                resultRanges.add(LongRange(xformRanges[index - 1].last + 1, xformRanges[index].first - 1))
                resultRanges.add(LongRange(xformRanges[index].first + factors[index], range.last + factors[index]))
                return -1   // break the loop
            }
            else -> {
                if (!resultRanges.contains(LongRange(xformRanges[index - 1].last + 1, xformRanges[index].first - 1)))
                    resultRanges.add(LongRange(xformRanges[index - 1].last + 1, xformRanges[index].first - 1))
                resultRanges.add(LongRange(xformRanges[index].first + factors[index], xformRanges[index].last + factors[index]))
                return 0   // continue the loop
            }
        }
    }

    enum class MapId {
        SeedToSoil, SoilToFertlzr, FertlzrToWater, WaterToLight, LightToTemp, TempToHumid, HumidToLctn;
        companion object {
            fun of(s: String) =
                when (s) {
                    "seed-to-soil"-> SeedToSoil; "soil-to-fertilizer"-> SoilToFertlzr; "fertilizer-to-water"-> FertlzrToWater
                    "water-to-light"-> WaterToLight; "light-to-temperature"-> LightToTemp; "temperature-to-humidity"-> TempToHumid
                    "humidity-to-location"-> HumidToLctn; else-> throw AocException("invalid input $s")
                }
        }
    }
}
