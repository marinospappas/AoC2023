package mpdev.springboot.aoc2023.solutions.day13

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day13: PuzzleSolver() {

    final override fun setDay() {
        day = 13
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var mirror: Mirror

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            mirror = Mirror(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            mirror = Mirror(inputData)
            mirror.reflections.forEach { mirror.checkReflection(it, mirror::listCompare1) }
            result = mirror.reflections.fold(0)
            { acc, r -> acc + r.reflLine * (if (r.reflType == ReflectionType.H) 100 else 1) }
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            mirror.reflections.forEach { mirror.checkReflection(it, mirror::listCompare2) }
            result = mirror.reflections.fold(0)
            { acc, r -> acc + r.reflLine * (if (r.reflType == ReflectionType.H) 100 else 1) }
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}