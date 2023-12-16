package mpdev.springboot.aoc2023.solutions.day16

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import mpdev.springboot.aoc2023.utils.GridUtils
import mpdev.springboot.aoc2023.utils.Point
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day16: PuzzleSolver() {

    final override fun setDay() {
        day = 16
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var lavaFloor: LavaFloor

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            lavaFloor = LavaFloor(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = lavaFloor.simulateBeam(Beam(Point(0,0), GridUtils.Direction.RIGHT))
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = lavaFloor.getPerimeterBeams().maxOf { lavaFloor.simulateBeam(it) }
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}