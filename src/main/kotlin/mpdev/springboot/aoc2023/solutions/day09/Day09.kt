package mpdev.springboot.aoc2023.solutions.day09

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureNanoTime

@Component
class Day09: PuzzleSolver() {

    final override fun setDay() {
        day = 9
    }

    init {
        setDay()
    }

    var result = 0L
    lateinit var oasis: Oasis

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureNanoTime {
            oasis = Oasis(inputData)
        }
        return Pair(elapsed/1000, "micro-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureNanoTime {
            result = oasis.failures.sumOf { oasis.predictNextValue(it) }
        }
        return PuzzlePartSolution(1, result.toString(), elapsed/1000, "micro-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureNanoTime {
            result = oasis.failures.sumOf { oasis.extrapolatePastValue(it) }
        }
        return PuzzlePartSolution(2, result.toString(), elapsed/1000, "micro-sec")
    }
}