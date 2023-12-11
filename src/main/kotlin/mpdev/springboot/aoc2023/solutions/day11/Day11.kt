package mpdev.springboot.aoc2023.solutions.day11

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day11: PuzzleSolver() {

    final override fun setDay() {
        day = 11
    }

    init {
        setDay()
    }

    var result = 0L
    lateinit var universe: Universe

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            universe = Universe(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            universe.expand(2)
            result = universe.measureDistances().sum()
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            universe = Universe(inputData)
            universe.expand(1000000)
            result = universe.measureDistances().sum()
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}