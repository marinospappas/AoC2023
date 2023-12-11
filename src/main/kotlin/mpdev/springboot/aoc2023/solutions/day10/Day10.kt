package mpdev.springboot.aoc2023.solutions.day10

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day10: PuzzleSolver() {

    final override fun setDay() {
        day = 10
    }

    init {
        setDay()
    }

    var result = 0L
    lateinit var pipeNetwork: PipeNetwork

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            pipeNetwork = PipeNetwork(inputData)
        }
        return Pair(elapsed, "mill-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            pipeNetwork.findLoop()
            result = pipeNetwork.loop.size / 2L
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "mill-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = pipeNetwork.findPointsInsideLoop().toLong()
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}