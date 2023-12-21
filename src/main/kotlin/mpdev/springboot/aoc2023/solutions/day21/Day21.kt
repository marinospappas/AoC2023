package mpdev.springboot.aoc2023.solutions.day21

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day21: PuzzleSolver() {

    final override fun setDay() {
        day = 21
    }

    init {
        setDay()
    }

    var result = 0L
    lateinit var farmPlan: FarmPlan

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            farmPlan = FarmPlan(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = farmPlan.traverseGrid(farmPlan.start, 64).size + 1L
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = farmPlan.solvePart2()
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}