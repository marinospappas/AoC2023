package mpdev.springboot.aoc2023.solutions.day03

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day03: PuzzleSolver() {

    final override fun setDay() {
        day = 3
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var engine: Engine

    override fun initSolver(): Pair<Long,String> {
        result = 0
        val elapsed = measureTimeMillis {
            engine = Engine(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = engine.findSumOfNumsAdjToSymbol()
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = engine.findSumOfGearBoxRatios()
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}