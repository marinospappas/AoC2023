package mpdev.springboot.aoc2023.solutions.dayxxexample

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class DayXX: PuzzleSolver() {

    final override fun setDay() {
        day = 3
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var fabric: Fabric

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            fabric = Fabric(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = fabric.findOverlappingPoints().size
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = fabric.findNonOverlappingClaim()
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }

}