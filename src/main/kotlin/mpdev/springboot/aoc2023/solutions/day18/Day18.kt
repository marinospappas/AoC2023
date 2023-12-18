package mpdev.springboot.aoc2023.solutions.day18

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day18: PuzzleSolver() {

    final override fun setDay() {
        day = 18
    }

    init {
        setDay()
    }

    var result = 0L
    lateinit var digPlan: DigPlan

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            digPlan = DigPlan(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = digPlan.digArea(digPlan.digDirections)
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = digPlan.digArea(digPlan.digDirections2)
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}