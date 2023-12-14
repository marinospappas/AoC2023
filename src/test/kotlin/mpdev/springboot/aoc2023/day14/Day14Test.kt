package mpdev.springboot.aoc2023.day14

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day14.Day14
import mpdev.springboot.aoc2023.solutions.day14.ReflectorDish
import mpdev.springboot.aoc2023.solutions.day14.Rock
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day14Test {

    private val day = 14                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day14()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var reflectorDish: ReflectorDish

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        reflectorDish = ReflectorDish(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input ans sets Grid up`() {
        reflectorDish.grid.print()
        assertThat(reflectorDish.grid.getDimensions()).isEqualTo(Pair(10,10))
        assertThat(reflectorDish.grid.getDataPoints().count()).isEqualTo(35)
    }

    @Test
    @Order(3)
    fun `Tilts plain Up`() {
        reflectorDish.rollAllUp()
        reflectorDish.grid.print()
        reflectorDish.calculateLoadUp().println()
        assertThat(reflectorDish.grid.getDataPoints().count { it.value == Rock.SPHERE && it.key.y == 0 }).isEqualTo(5)
        assertThat(reflectorDish.grid.getDataPoints().count { it.value == Rock.SPHERE && it.key.y == 1 }).isEqualTo(2)
        assertThat(reflectorDish.grid.getDataPoints().count { it.value == Rock.SPHERE && it.key.y == 2 }).isEqualTo(4)
        assertThat(reflectorDish.grid.getDataPoints().count { it.value == Rock.SPHERE && it.key.y == 3 }).isEqualTo(3)
        assertThat(reflectorDish.grid.getDataPoints().count { it.value == Rock.SPHERE && it.key.y == 6 }).isEqualTo(3)
        assertThat(reflectorDish.grid.getDataPoints().count { it.value == Rock.SPHERE && it.key.y == 7 }).isEqualTo(1)
        assertThat(reflectorDish.grid.countOf(Rock.SPHERE)).isEqualTo(18)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("136")
    }

    @Test
    @Order(6)
    fun `Spins the Plain in all directions`() {
        reflectorDish.spinAll()
        reflectorDish.grid.print()
        assertThat(reflectorDish.grid.getDataPoints().count { it.value == Rock.SPHERE && it.key.y == 1 }).isEqualTo(1)
        assertThat(reflectorDish.grid.getDataPoints().count { it.value == Rock.SPHERE && it.key.y == 2 }).isEqualTo(2)
        assertThat(reflectorDish.grid.getDataPoints().count { it.value == Rock.SPHERE && it.key.y == 3 }).isEqualTo(2)
        assertThat(reflectorDish.grid.getDataPoints().count { it.value == Rock.SPHERE && it.key.y == 4 }).isEqualTo(3)
        assertThat(reflectorDish.grid.getDataPoints().count { it.value == Rock.SPHERE && it.key.y == 5 }).isEqualTo(2)
        assertThat(reflectorDish.grid.getDataPoints().count { it.value == Rock.SPHERE && it.key.y == 6 }).isEqualTo(1)
        assertThat(reflectorDish.grid.getDataPoints().count { it.value == Rock.SPHERE && it.key.y == 7 }).isEqualTo(4)
        assertThat(reflectorDish.grid.getDataPoints().count { it.value == Rock.SPHERE && it.key.y == 8 }).isEqualTo(1)
        assertThat(reflectorDish.grid.getDataPoints().count { it.value == Rock.SPHERE && it.key.y == 9 }).isEqualTo(2)
        assertThat(reflectorDish.grid.countOf(Rock.SPHERE)).isEqualTo(18)
    }

    @Test
    @Order(7)
    fun `Finds Repeat Cycle`() {
        val (first, second, list) = reflectorDish.findRepeatCycle()
        println("start $first, next $second")
        println("results: $list")
        assertThat(first).isEqualTo(195)
        assertThat(second).isEqualTo(202)
    }


    @Test
    @Order(8)
    fun `Solves Part 2`() {
         assertThat(puzzleSolver.solvePart2().result).isEqualTo("64")
    }
}
