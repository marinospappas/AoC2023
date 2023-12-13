package mpdev.springboot.aoc2023.solutions.day14

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day14: PuzzleSolver() {

    final override fun setDay() {
        day = 14
    }

    init {
        setDay()
    }

    var result = 0L
    lateinit var xxxx: Xxxx

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            xxxx = Xxxx(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            //result =
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            //result =
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}