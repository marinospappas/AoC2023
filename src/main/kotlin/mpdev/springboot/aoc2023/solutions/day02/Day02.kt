package mpdev.springboot.aoc2023.solutions.day02

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureNanoTime

@Component
class Day02: PuzzleSolver() {

    final override fun setDay() {
        day = 2
    }

    init {
        setDay()
    }

    var result = 0

    override fun initSolver(): Pair<Long,String> {
        result = 0
        val elapsed = measureNanoTime {}
        return Pair(elapsed/1000, "micro-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureNanoTime {

        }
        return PuzzlePartSolution(1, result.toString(), elapsed/1000, "micro-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureNanoTime {

        }
        return PuzzlePartSolution(2, result.toString(), elapsed/1000, "micro-sec")
    }
}