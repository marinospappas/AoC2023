package mpdev.springboot.aoc2023.day13

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day13.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import java.io.File

class Day13Test {

    private val day = 13                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day13()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var mirror: Mirror

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        mirror = Mirror(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input ans sets Mirrored Patterns`() {
        mirror.reflections.forEach { it.print() }
        assertThat(mirror.reflections.size).isEqualTo(3)
    }

    @Test
    @Order(3)
    fun `Finds Lines of Reflection`() {
        mirror.reflections.forEach { mirror.checkReflection(it, mirror::listCompare1) }
        mirror.reflections.forEach { it.print() }
        assertThat(mirror.reflections[0].reflType).isEqualTo(ReflectionType.H)
        assertThat(mirror.reflections[0].reflLine).isEqualTo(12)
        assertThat(mirror.reflections[1].reflType).isEqualTo(ReflectionType.V)
        assertThat(mirror.reflections[1].reflLine).isEqualTo(5)
        assertThat(mirror.reflections[2].reflType).isEqualTo(ReflectionType.H)
        assertThat(mirror.reflections[2].reflLine).isEqualTo(4)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        puzzleSolver.inputData = File("src/test/resources/inputdata/input13.txt").readLines()
            .stream().skip(14).toList()
        puzzleSolver.initSolver()
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("405")
    }

    @Test
    @Order(6)
    fun `Finds Lines of Reflection with Defects`() {
        mirror.reflections.forEach { mirror.checkReflection(it, mirror::listCompare2) }
        mirror.reflections.forEach { it.print() }
        assertThat(mirror.reflections[1].reflType).isEqualTo(ReflectionType.H)
        assertThat(mirror.reflections[1].reflLine).isEqualTo(3)
        assertThat(mirror.reflections[2].reflType).isEqualTo(ReflectionType.H)
        assertThat(mirror.reflections[2].reflLine).isEqualTo(1)
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        puzzleSolver.inputData = File("src/test/resources/inputdata/input13.txt").readLines()
            .stream().skip(14).toList()
        puzzleSolver.initSolver()
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("400")
    }
}
