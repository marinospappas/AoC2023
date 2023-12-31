package mpdev.springboot.aoc2023.day18

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day18.AoCInput
import mpdev.springboot.aoc2023.solutions.day18.Day18
import mpdev.springboot.aoc2023.solutions.day18.DigInstr
import mpdev.springboot.aoc2023.solutions.day18.DigPlan
import mpdev.springboot.aoc2023.utils.GridUtils
import mpdev.springboot.aoc2023.utils.InputUtils
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day18Test {

    private val day = 18                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day18()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var digPlan: DigPlan

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        digPlan = DigPlan(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(3)
    fun `Reads Input ans sets Dig Plan`() {
        println("input transformed")
        inputLines.forEach { InputUtils(AoCInput::class.java).transform(it).println() }
        println("input to json")
        inputLines.map { InputUtils(AoCInput::class.java).transform(it) }
            .filterNot { InputUtils.skipEmptyLines && it.isEmpty() }
            .joinToString(",", "[", "]") { InputUtils(AoCInput::class.java).toJson(it) }
                .println()
        println("Part 1")
        digPlan.digDirections.forEach { it.println() }
        println("Part 2")
        digPlan.digDirections2.forEach { it.println() }
        assertThat(digPlan.digDirections.size).isEqualTo(14)
        assertThat(digPlan.digDirections2.size).isEqualTo(14)
        assertThat(digPlan.digDirections2.first().length).isEqualTo(461937)
        assertThat(digPlan.digDirections2.last().length).isEqualTo(500254)
    }

    @Test
    @Order(3)
    fun `Digs Trench`() {
        digPlan.digTrench()
        digPlan.grid.print()
        val volume = digPlan.digVolume(digPlan.digDirections).also { it.println() }
        assertThat(volume).isEqualTo(62)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("62")
    }

    @Test
    @Order(6)
    fun `Calculates Dig Volume using Area from Picks Theorem`() {
        var input = listOf(
            DigInstr(GridUtils.Direction.RIGHT, 5),
            DigInstr(GridUtils.Direction.DOWN, 4),
            DigInstr(GridUtils.Direction.LEFT, 5),
            DigInstr(GridUtils.Direction.UP, 4)
        )
        var volume = digPlan.digVolume(input).also { it.println() }
        assertThat(volume).isEqualTo(30)
        input = listOf(
            DigInstr(GridUtils.Direction.RIGHT, 3),
            DigInstr(GridUtils.Direction.DOWN, 4),
            DigInstr(GridUtils.Direction.LEFT, 5),
            DigInstr(GridUtils.Direction.UP, 2),
            DigInstr(GridUtils.Direction.RIGHT, 2),
            DigInstr(GridUtils.Direction.UP, 2)
        )
        volume = digPlan.digVolume(input).also { it.println() }
        assertThat(volume).isEqualTo(26)
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("952408144115")
    }
}
