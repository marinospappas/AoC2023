package mpdev.springboot.aoc2023.solutions.day20

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import mpdev.springboot.aoc2023.utils.lcm
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day20: PuzzleSolver() {

    final override fun setDay() {
        day = 20
    }

    init {
        setDay()
    }

    var result = 0L
    lateinit var pulseProcessor: PulseProcessor

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            pulseProcessor = PulseProcessor(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = pulseProcessor.countPulsesPart1().toLong()
        }
        return PuzzlePartSolution(1, result.toString(), elapsed, "milli-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            pulseProcessor = PulseProcessor(inputData)
            result = pulseProcessor.identifyighPulseCyclesForFinalConjuction().lcm()
        }
        return PuzzlePartSolution(2, result.toString(), elapsed, "milli-sec")
    }
}