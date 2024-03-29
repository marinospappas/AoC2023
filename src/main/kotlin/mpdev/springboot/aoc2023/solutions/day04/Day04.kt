package mpdev.springboot.aoc2023.solutions.day04

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day04: PuzzleSolver() {

    final override fun setDay() {
        day = 4
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var scratchCardGame: ScratchCardGame

    override fun initSolver(): Pair<Long,String> {
        result = 0
        val elapsed = measureTimeMillis {
            scratchCardGame = ScratchCardGame(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = scratchCardGame.playGamePart1()
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = scratchCardGame.playGamePart2()
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}