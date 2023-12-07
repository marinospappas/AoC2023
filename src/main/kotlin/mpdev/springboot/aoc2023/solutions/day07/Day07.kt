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

    var result = 0L
    lateinit var camelCards: CamelCards

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            camelCards = CamelCards(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = camelCards.winnings()
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = camelCards.winnings(joker = true)
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}