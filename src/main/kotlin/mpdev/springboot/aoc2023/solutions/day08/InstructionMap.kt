package mpdev.springboot.aoc2023.solutions.day08

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Serializable
@AocInClass(delimiters = ["=", ","], skipLines = 1, removePatterns = ["\\(", "\\)"])
data class AoCInput(
    // AAA = (BBB, CCC)
    // 0      1    2
    @AocInField(0) val start: String,
    @AocInField(1) val left: String,
    @AocInField(2) val right: String
)

class InstructionMap(input: List<String>) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)
    var debug = false

    val instructions: List<Char> = input[0].toCharArray().toList()
    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    val steps: Map<String, Pair<String, String>> = aocInputList.associate { it.start to Pair(it.left, it.right) }
    private val instrList: CircularList<Char> = CircularList(instructions.toMutableList())

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
    }
}
