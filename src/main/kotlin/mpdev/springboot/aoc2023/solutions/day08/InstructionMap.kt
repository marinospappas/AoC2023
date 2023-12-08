package mpdev.springboot.aoc2023.solutions.day08

import mpdev.springboot.aoc2023.utils.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class InstructionMap(input: List<String>) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    var debug = false
    val instructions: List<Char>
    val steps: Map<String, Pair<String, String>>
    val instrList: CircularList<Char>

    init {
        val (first, second) = processInput(input)
        instructions = first
        steps = second
        instrList = CircularList(instructions.toMutableList())
    }

    fun followSteps(start: String, endCondition: (String) -> Boolean): Long {
        var current = start
        var count = 0L
        while (!endCondition(current)) {
            if (debug) current.println()
            val lr = instrList.getCurrent()
            current = if (lr == LEFT) steps[current]?.first!! else steps[current]?.second!!
            instrList.incrCurPos(1)
            ++count
        }
        if (debug) current.println()
        return count
    }

    fun followStepsConcurrently(): List<Long> {
        val startList = steps.keys.filter { it.last() == 'A' }
        val stepsList = mutableListOf<Long>()
        // find number of steps for each start independently
        startList.forEach { start ->
            stepsList.add(followSteps(start) { s -> s.last() == 'Z' } )
            log.info("{} to ..Z in {} steps", start, stepsList.last())
        }
        return stepsList
    }

    companion object {

        const val LEFT = 'L'

        fun processInput(input: List<String>): Pair<List<Char>, Map<String, Pair<String, String>>> {
            val stepsMap = mutableMapOf<String, Pair<String, String>>()
            var instruction = listOf<Char>()
            var firstLine = true
            input.filter { it.isNotEmpty() }.forEach { line ->
                if (firstLine) {
                    instruction = line.toCharArray().toList()
                    firstLine = false
                    return@forEach
                }
                // AAA = (BBB, BBB)
                val match = Regex("""([\dA-Z]+) = \(([\dA-Z]+), ([\dA-Z]+)\)""").find(line)
                val (key, left, right) = match!!.destructured
                stepsMap[key] = Pair(left, right)
            }
            return Pair(instruction, stepsMap)
        }
    }
}