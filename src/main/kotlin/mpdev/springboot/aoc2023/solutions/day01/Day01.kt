package mpdev.springboot.aoc2023.solutions.day01

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
@Order(1)
class Day01: PuzzleSolver() {

    private lateinit var calories: List<List<Int>>

    init {
        day = 1
    }

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
        var elfList = mutableListOf<Int>()
        inputLines.forEach { line ->
            if (line.isNotEmpty())
                elfList.add(line.toInt())
            else {
                result.add(elfList)
                elfList = mutableListOf()
            }
        }
        result.add(elfList)
        return result
    }
}