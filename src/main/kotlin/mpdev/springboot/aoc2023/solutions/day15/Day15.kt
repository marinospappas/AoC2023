package mpdev.springboot.aoc2023.solutions.day15

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day15: PuzzleSolver() {

    final override fun setDay() {
        day = 15
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var lensFacility: LensFacility

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            lensFacility = LensFacility(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = lensFacility.initInstructions.sumOf { it.hashD152023() }

        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            lensFacility.initInstructions.forEach { lensFacility.executeStep(it) }
            var boxIndx = 1
            result = lensFacility.boxes.map { lensFacility.boxFocalPwr(it) }
                .fold(0) { acc, fp -> acc + fp * boxIndx++ }
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}