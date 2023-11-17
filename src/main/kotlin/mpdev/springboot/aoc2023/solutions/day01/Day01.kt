package mpdev.springboot.aoc2023.solutions.day01

import mpdev.springboot.aoc2023.model.PuzzlePartSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureNanoTime

@Component
class Day01: PuzzleSolver() {

    final override fun setDay() {
        day = 1
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var intList: IntArray

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureNanoTime {
            intList = IntArray(inputData.size) { Integer.parseInt(inputData[it]) }
        }
        return Pair(elapsed/1000, "micro-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureNanoTime {
            result = intList.sum()
        }
        return PuzzlePartSolution(1, result.toString(), elapsed/1000, "micro-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureNanoTime {
            result = applyChangeAndFindDuplicate()
        }
        return PuzzlePartSolution(2, result.toString(), elapsed/1000, "micro-sec")
    }

    fun applyChangeAndFindDuplicate(): Int {
        val freqSet = mutableSetOf<Int>()
        var freq = 0
        var index = 0
        while (true) {
            freq += intList[index++]
            if (freqSet.contains(freq))
                return freq
            freqSet.add(freq)
            if (index > intList.lastIndex)
                index = 0
        }
    }
}