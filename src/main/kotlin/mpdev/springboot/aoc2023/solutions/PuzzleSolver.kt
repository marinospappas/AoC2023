package mpdev.springboot.aoc2023.solutions

import mpdev.springboot.aoc2023.model.PuzzleSolution

interface PuzzleSolver {

    fun solve(): PuzzleSolution
}