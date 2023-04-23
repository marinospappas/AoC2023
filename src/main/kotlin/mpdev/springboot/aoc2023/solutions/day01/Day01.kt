package mpdev.springboot.aoc2023.solutions.day01

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
@Order(1)
class Day01: PuzzleSolver() {

    init {
        day = 1         ////// update this when a puzzle solver for a new day is implemented
    }

    private lateinit var calories: List<List<Int>>

    override fun part1(): PuzzlePartSolution {
        calories = processInput(inputData)
        var result = 0
        val elapsed = measureTimeMillis {
            result = calories.maxByOrNull { it.sum() }!!.sum()
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun part2(): PuzzlePartSolution {
        calories = processInput(inputData)
        var result = 0
        val elapsed = measureTimeMillis {
            result = calories.sortedBy { it.sum() }.reversed().subList(0,3).sumOf { it.sum() }
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }

    fun processInput(inputLines: List<String>): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        var calorieList = mutableListOf<Int>()
        inputLines.forEach { line ->
            if (line.isNotEmpty())
                calorieList.add(line.toInt())
            else {
                result.add(calorieList)
                calorieList = mutableListOf()
            }
        }
        result.add(calorieList)
        return result
    }
}