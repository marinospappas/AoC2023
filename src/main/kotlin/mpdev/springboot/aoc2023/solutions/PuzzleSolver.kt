package mpdev.springboot.aoc2023.solutions

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.model.PuzzleSolution
import org.springframework.beans.factory.annotation.Autowired

abstract class PuzzleSolver {

    @Autowired
    lateinit var inputDataReader: InputDataReader

    lateinit var inputData: List<String>

    var day: Int = 0

    fun solve(): PuzzleSolution {
        inputData = inputDataReader.read(day)
        return PuzzleSolution(day = day, solution = setOf(part1(), part2()))
    }

    abstract fun part1(): PuzzlePartSolution
    abstract fun part2(): PuzzlePartSolution

}