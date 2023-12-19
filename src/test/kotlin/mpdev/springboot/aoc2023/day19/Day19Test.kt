package mpdev.springboot.aoc2023.day19

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.solutions.day19.Day19
import mpdev.springboot.aoc2023.solutions.day19.MPart
import mpdev.springboot.aoc2023.solutions.day19.MachineParts
import mpdev.springboot.aoc2023.solutions.day19.WFRes
import mpdev.springboot.aoc2023.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day19Test {

    private val day = 19                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day19()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)
    private lateinit var machineParts: MachineParts

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
        machineParts = MachineParts(inputLines)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(3)
    fun `Reads Input ans sets Parts set and Rules set`() {
        println("Workflows")
        machineParts.workflows.forEach { it.println() }
        println("Machine Parts")
        machineParts.partsList.forEach { it.println() }
        assertThat(machineParts.workflows.size).isEqualTo(11)
        assertThat(machineParts.partsList.size).isEqualTo(5)
    }

    @Test
    @Order(3)
    fun `Runs Workflows`() {
        machineParts.partsList.forEach { part ->
            print("$part -> ")
            machineParts.runWorkFlows(part).println()
        }
        val wfResult = machineParts.partsList.map { machineParts.runWorkFlows(it) }
        assertThat(wfResult).isEqualTo(listOf(WFRes.A, WFRes.R, WFRes.A, WFRes.R, WFRes.A))
        val sumOfAttr = "xmas".toCharArray().map { machineParts.partsList
            .filter { part -> machineParts.runWorkFlows(part) == WFRes.A } .map { part -> part.get(it) }
        }.flatten().sum() .also { it.println() }
        assertThat(sumOfAttr).isEqualTo(19114)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("19114")
    }

    @Test
    @Order(6)
    fun `Processes All Ranges to find Acceptable ones`() {
        machineParts.processRanges()
        machineParts.acceptedRanges.println()
        machineParts.acceptedRanges.sumOf { machineParts.rangeCountCombis(it) }.println()
        machineParts.acceptedRanges.forEach { r ->
            print("verifying range group: $r : ")
            var check = machineParts.runWorkFlows(MPart(r['x']?.first!!, r['m']?.first!!, r['a']?.first!!, r['s']?.first!!))
                .also { print("start: $it end: ") }
            assertThat(check).isEqualTo(WFRes.A)
            check = machineParts.runWorkFlows(MPart(r['x']?.last!!, r['m']?.last!!, r['a']?.last!!, r['s']?.last!!))
                .also { it.println() }
            assertThat(check).isEqualTo(WFRes.A)
        }
    }

    @Test
    @Order(8)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("167409079868000")
    }
}
