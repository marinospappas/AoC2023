package mpdev.springboot.aoc2023.solutions.day08

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

@Component
class Day08: PuzzleSolver() {

    final override fun setDay() {
        day = 8
    }

    init {
        setDay()
    }

    var result = 0L
    lateinit var instructionMap: InstructionMap

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            instructionMap = InstructionMap(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureNanoTime {
            result = instructionMap.followSteps("AAA") { s -> s == "ZZZ" }
        }
        return PuzzlePartSolution(1, result.toString(), elapsed/1000, "micro-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = instructionMap.followStepsConcurrently()
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}