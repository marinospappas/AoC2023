package mpdev.springboot.aoc2023.solutions.day24

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day24: PuzzleSolver() {

    final override fun setDay() {
        day = 24
    }

    init {
        setDay()
    }

    var result = 0L
    lateinit var hailStones: HailStones

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            hailStones = HailStones(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = hailStones.calculateIntersections().toLong()
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            val `throw` = hailStones.calculateThrow()
            result = `throw`.position.toList().sum()
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}