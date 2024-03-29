package mpdev.springboot.aoc2023.solutions.day22

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import mpdev.springboot.aoc2023.utils.println
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day22: PuzzleSolver() {

    final override fun setDay() {
        day = 22
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var jengaBricks: JengaBricks

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            jengaBricks = JengaBricks(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            jengaBricks.landAllBricks()
            result = jengaBricks.getRemovableBricks().size
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            val collapseList = jengaBricks.bricks.indices.toSet() - jengaBricks.getRemovableBricks()
            result = collapseList.sumOf { jengaBricks.determineBricksToFall(it) }
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}