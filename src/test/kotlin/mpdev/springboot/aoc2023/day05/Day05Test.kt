package mpdev.springboot.aoc2023.day05

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day05.Almanac
import mpdev.springboot.aoc2023.solutions.day05.AoCInput
import mpdev.springboot.aoc2023.solutions.day05.Day05
import mpdev.springboot.aoc2023.utils.InputUtils
import mpdev.springboot.aoc2023.utils.InputUtils.Companion.skipEmptyLines
import mpdev.springboot.aoc2023.utils.InputUtils.Companion.skipLines
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day05Test {

    private val day = 5                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day05()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var almanac: Almanac

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        almanac = Almanac(inputLines)
        puzzleSolver.initSolver()
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input ans sets Mappings`() {
        println("input transformed")
        val inputTransformed = inputLines.subList(1, inputLines.size).stream().skip(skipLines.toLong()).toList()
            .map { InputUtils(AoCInput::class.java).transform(it) }
            .filterNot { skipEmptyLines && it.isEmpty() }.also { it.forEach { s -> s.println() } }
        println("input to json")
        inputTransformed.forEach { InputUtils(AoCInput::class.java).toJson(it).println() }
        println("seeds list")
        almanac.seedsList.println()
        Almanac.MapId.values().forEach { state ->
            println(state)
            almanac.maps[state]?.forEach { it.println() }
        }
        almanac.maps.values.forEach { l ->
            for (i in 0 .. l.lastIndex-1) {
                assertTrue(l[i].first.last < l[i+1].first.first)
            }
        }
    }

    @Test
    @Order(3)
    fun `Resolves Mappings for each Seed`() {
        val expected = listOf(
            listOf<Long>(81, 81, 81, 74, 78, 78, 82),
            listOf<Long>(14, 53, 49, 42, 42, 43, 43),
            listOf<Long>(57, 57, 53, 46, 82, 82, 86),
            listOf<Long>(13, 52, 41, 34, 34, 35, 35)
        )
        val almanac = Almanac(inputLines)
        almanac.seedsList.indices.forEach { indx ->
            val seed = almanac.seedsList[indx]
            print("seed $seed: ")
            var current = seed
            Almanac.MapId.values().forEach { map ->
                current = almanac.getMapping(map, current)
                print("$map $current, ")
                assertThat(current).isEqualTo(expected[indx][map.ordinal])
            }
            println("")
        }
        assertThat(almanac.seedsList.minOf { almanac.getLocation(it) }.also { it.println() })
            .isEqualTo(35)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("35")
    }

    @ParameterizedTest
    @MethodSource("transformRangeArguments")
    @Order(6)
    fun `Transforms a Range`(inputRange: LongRange, xformMap: List<Pair<LongRange,Long>>, expected: List<LongRange>) {
        val result = almanac.transformRange(inputRange, xformMap).also { it.println() }
        assertThat(result).isEqualTo(expected)
    }

    private fun transformRangeArguments(): Stream<Arguments> =
        Stream.of(
            // 1 range returned
            Arguments.of(2L..6L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(2L..6L)),
            Arguments.of(22L..26L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(22L..26L)),
            Arguments.of(42L..46L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(42L..46L)),
            Arguments.of(62L..66L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(62L..66L)),

            Arguments.of(13L..15L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(15L..17L)),
            Arguments.of(33L..40L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(37L..44L)),
            Arguments.of(53L..55L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(59L..61L)),

            // 2 ranges returned
            Arguments.of(5L..15L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(5L..9L, 12L..17L)),
            Arguments.of(25L..40L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(25L..29L, 34L..44L)),
            Arguments.of(45L..55L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(45L..49L, 56L..61L)),

            Arguments.of(15L..25L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(17L..22L, 21L..25L)),
            Arguments.of(30L..45L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(34L..44L, 41L..45L)),
            Arguments.of(55L..65L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(61L..66L, 61L..65L)),

            // 3 ranges returned
            Arguments.of(5L..25L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(5L..9L, 12L..22L, 21L..25L)),
            Arguments.of(25L..45L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(25L..29L, 34L..44L, 41L..45L)),
            Arguments.of(45L..65L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(45L..49L, 56L..66L, 61L..65L)),

            Arguments.of(15L..40L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(17L..22L, 21L..29L, 34L..44L)),
            Arguments.of(35L..55L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(39L..44L, 41L..49L, 56L..61L)),

            // 4 ranges returned
            Arguments.of(5L..35L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(5L..9L, 12L..22L, 21L..29L, 34L..39L)),
            Arguments.of(25L..55L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(25L..29L, 34L..44L, 41L..49L, 56L..61L)),

            Arguments.of(15L..45L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(17L..22L, 21L..29L, 34L..44L, 41L..45L)),
            Arguments.of(35L..65L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(39L..44L, 41L..49L, 56L..66L, 61L..65L)),

            // 5 ranges returned
            Arguments.of(5L..45L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(5L..9L, 12L..22L, 21L..29L, 34L..44L, 41L..45L)),
            Arguments.of(25L..65L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(25L..29L, 34L..44L, 41L..49L, 56L..66L, 61L..65L)),

            Arguments.of(15L..55L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(17L..22L, 21L..29L, 34L..44L, 41L..49L, 56L..61L)),

            // 6 ranges returned
            Arguments.of(5L..55L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(5L..9L, 12L..22L, 21L..29L, 34L..44L, 41L..49L, 56L..61L)),

            Arguments.of(15L..65L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(17L..22L, 21L..29L, 34L..44L, 41L..49L, 56L..66L, 61L..65L)),

            // 7 ranges returned
            Arguments.of(5L..65L, listOf(Pair(10L..20L, 2L), Pair(30L..40L, 4L), Pair(50L..60L, 6L)),
                listOf(5L..9L, 12L..22L, 21L..29L, 34L..44L, 41L..49L, 56L..66L, 61L..65L))
        )

    @Test
    @Order(6)
    fun `Transforms a List of Ranges via Multiple Reference Maps`() {
        var ranges = almanac.seedRanges
        for (mapId in Almanac.MapId.values()) {
            println(mapId)
            val result = almanac.transformRangeList(ranges, almanac.maps[mapId]!!).also { it.println() }
            val expected = ranges.map { r ->
                r.map { seed -> almanac.getMapping(mapId, seed) }
            }.also { it.println() }
            assertThat(result.map { it.toList() }.flatten().sorted()).isEqualTo(expected.flatten().sorted())
            ranges = result
        }
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("46")
        assertThat(almanac.getMinLocationFromRange()).isEqualTo(46)
    }
}
