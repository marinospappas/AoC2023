package mpdev.springboot.aoc2023.solutions.day12

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day12: PuzzleSolver() {

    final override fun setDay() {
        day = 12
    }

    init {
        setDay()
    }

    var result = 0L
    lateinit var springs: SpringCondition

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            springs = SpringCondition(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = springs.records.sumOf { springs.getCount(it) }
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = springs.records.sumOf { springs.getCount(it * 5) }
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}