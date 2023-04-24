package mpdev.springboot.aoc2023.solutions.day01

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
@Order(1)
class Day01(@Autowired var inputProcessor: InputProcessor01): PuzzleSolver() {

    override fun setDay() {
        day = 1         ////// update this when a puzzle solver for a new day is implemented
    }

    private lateinit var calories: List<List<Int>>
    var result = 0

    override fun initSolver() {
        calories = inputProcessor.process(inputData)
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = calories.maxByOrNull { it.sum() }!!.sum()
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = calories.sortedBy { it.sum() }.reversed().subList(0,3).sumOf { it.sum() }
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }

}