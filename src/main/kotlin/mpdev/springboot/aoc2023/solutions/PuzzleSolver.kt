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
        setDay()
        inputData = inputDataReader.read(day)
        initSolver()
        return PuzzleSolution(day = day, solution = setOf(solvePart1(), solvePart2()))
    }

    abstract fun setDay()
    abstract fun initSolver()
    abstract fun solvePart1(): PuzzlePartSolution
    abstract fun solvePart2(): PuzzlePartSolution

}