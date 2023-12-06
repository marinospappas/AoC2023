package mpdev.springboot.aoc2023.solutions.day07

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day07: PuzzleSolver() {

    final override fun setDay() {
        day = 7
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var xxxx: Xxxx

    override fun initSolver(): Pair<Long,String> {
        result = 0
        val elapsed = measureTimeMillis {

        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {

        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {

        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}