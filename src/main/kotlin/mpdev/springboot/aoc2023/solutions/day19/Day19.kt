package mpdev.springboot.aoc2023.solutions.day19

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day19: PuzzleSolver() {

    final override fun setDay() {
        day = 19
    }

    init {
        setDay()
    }

    var result = 0L
    lateinit var machineParts: MachineParts

    override fun initSolver(): Pair<Long, String> {
        val elapsed = measureTimeMillis {
            machineParts = MachineParts(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = machineParts.sumOfAcceptedAttributes().toLong()
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            machineParts.processRanges()
            result = machineParts.acceptedRanges.sumOf { machineParts.rangeCountCombis(it) }
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}