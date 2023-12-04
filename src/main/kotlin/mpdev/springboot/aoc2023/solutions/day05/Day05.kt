package mpdev.springboot.aoc2023.solutions.day05

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day05: PuzzleSolver() {

    final override fun setDay() {
        day = 5
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var xxx: XXX

    override fun initSolver(): Pair<Long,String> {
        result = 0
        val elapsed = measureTimeMillis {
            xxx = XXX(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            //result = xxx.
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            //result = xxx.
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}