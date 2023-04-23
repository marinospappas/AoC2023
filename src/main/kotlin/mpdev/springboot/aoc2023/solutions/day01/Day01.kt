package mpdev.springboot.aoc2023.solutions.day01

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.model.PuzzleSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(1)
class Day01: PuzzleSolver {

    override fun solve(): PuzzleSolution {
        return PuzzleSolution(day = 1, solution = setOf(PuzzlePartSolution()))
    }
}