package mpdev.springboot.aoc2023.solutions

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.model.PuzzleSolution
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

abstract class PuzzleSolver {

    protected val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var inputDataReader: InputDataReader

    lateinit var inputData: List<String>

    var day: Int = 0

    fun solve(): PuzzleSolution {
        log.info("solver for day {} called", day)
        inputData = inputDataReader.read(day)
        val initTime = initSolver()
        val part1 = solvePart1()
        val part2 = solvePart2()
        log.info("day {} part 1 answer: {} part 2 answer: {}", day, part1.result, part2.result)
        return PuzzleSolution(day = day, initTime = initTime.first, initTimeUnit = initTime.second,
            solution = setOf(part1, part2))
    }

    abstract fun setDay()
    abstract fun initSolver(): Pair<Long,String>
    abstract fun solvePart1(): PuzzlePartSolution
    abstract fun solvePart2(): PuzzlePartSolution

}