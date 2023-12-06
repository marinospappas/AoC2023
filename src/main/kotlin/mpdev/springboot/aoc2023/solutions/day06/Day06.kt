package mpdev.springboot.aoc2023.solutions.day06

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day06: PuzzleSolver() {

    final override fun setDay() {
        day = 6
    }

    init {
        setDay()
    }

    var result = 0L
    lateinit var boatRace: BoatRace

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            boatRace = BoatRace(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = boatRace.races.map { boatRace.minMaxChargeTime(it) }.map { it.second - it.first + 1 }.reduce(Long::times)
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = boatRace.minMaxChargeTime(boatRace.setupLongRace()).let { it.second - it.first + 1 }
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}