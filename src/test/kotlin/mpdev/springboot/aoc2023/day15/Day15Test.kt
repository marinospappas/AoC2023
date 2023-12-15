package mpdev.springboot.aoc2023.day15

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day15.Day15
import mpdev.springboot.aoc2023.solutions.day15.LensFacility
import mpdev.springboot.aoc2023.solutions.day15.hashD152023
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day15Test {

    private val day = 15                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day15()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var lensFacility: LensFacility

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        lensFacility = LensFacility(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input ans sets Instructions`() {
        lensFacility.initInstructions.forEach { it.println() }
        assertThat(lensFacility.initInstructions.size).isEqualTo(11)
    }

    @Test
    @Order(3)
    fun `Calculates Hash`() {
        var hash = "HASH".hashD152023().also { it.println() }
        assertThat(hash).isEqualTo(52)
        val expected = listOf(30, 253, 97, 47, 14, 180, 9, 197, 48, 214, 231)
        lensFacility.initInstructions.indices.forEach { i ->
            hash = lensFacility.initInstructions[i].hashD152023()
            assertThat(hash).isEqualTo(expected[i])
        }
        lensFacility.initInstructions.sumOf { it.hashD152023() }.println()
    }

    @Test
    @Order(4)
    fun `Calculates sum of Hashes`() {
        val sumOfHash = lensFacility.initInstructions.sumOf { it.hashD152023() }.also { it.println() }
        assertThat(sumOfHash).isEqualTo(1320)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("1320")
    }

    @Test
    @Order(6)
    fun `Places Lenses in Boxes`() {
        val expected = listOf(5, 0,0, 35)
        lensFacility.initInstructions.forEach { i ->
            lensFacility.executeStep(i)
            i.println()
            lensFacility.boxes.indices.forEach {
                val box = lensFacility.boxes[it]
                if (box.isNotEmpty()) {
                    println("box : $it lenses: $box focal Pwr: ${lensFacility.boxFocalPwr(box)}")
                }
            }
        }
        var index = 1
        lensFacility.boxes.indices.forEach {
            val box = lensFacility.boxes[it]
            assertThat(lensFacility.boxFocalPwr(box)).isEqualTo(if (box.isNotEmpty()) expected[it] else 0)
        }
        lensFacility.boxes.map { lensFacility.boxFocalPwr(it) }
            .fold(0) { acc, fp -> acc + fp * index++ }.println()
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
         assertThat(puzzleSolver.solvePart2().result).isEqualTo("145")
    }
}
