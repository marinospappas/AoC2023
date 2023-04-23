package mpdev.springboot.aoc2023.solutions

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.model.PuzzleSolution

abstract class PuzzleSolver {

    lateinit var inputData: List<String>

    var day: Int = 0

    fun solve(): PuzzleSolution = PuzzleSolution(day = day, solution = setOf(part1(), part2()))

    abstract fun part1(): PuzzlePartSolution
    abstract fun part2(): PuzzlePartSolution

}