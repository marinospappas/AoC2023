package mpdev.springboot.aoc2023.solutions.day25

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day25: PuzzleSolver() {

    final override fun setDay() {
        day = 25
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var wiringDiagram: WiringDiagram

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            wiringDiagram = WiringDiagram(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = wiringDiagram.breakConnectionsV2().let { it.first* it.second }
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        return PuzzlePartSolution(2, "MERRY CHRISTMAS!!", 0, "milli-sec")
    }
}