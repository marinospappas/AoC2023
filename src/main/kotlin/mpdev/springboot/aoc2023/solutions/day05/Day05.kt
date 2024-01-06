package mpdev.springboot.aoc2023.solutions.day05

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day05: PuzzleSolver() {

    final override fun setDay() {
        day = 5
    }

    init {
        setDay()
    }

    var result = 0L
    lateinit var almanac: Almanac

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            almanac = Almanac(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = almanac.seedsList.minOf { almanac.getLocation(it) }
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = almanac.transformRangesThroughAllMaps().first().first
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}