package mpdev.springboot.aoc2023.solutions.day17

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day17: PuzzleSolver() {

    final override fun setDay() {
        day = 17
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var cityMap: CityMap

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            cityMap = CityMap(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = cityMap.findMinPath().minCost
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            cityMap.minStraightSteps = 4
            cityMap.maxStraightSteps = 10
            result = cityMap.findMinPath2().minCost
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}