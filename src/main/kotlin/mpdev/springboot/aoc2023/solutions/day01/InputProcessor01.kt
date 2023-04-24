package mpdev.springboot.aoc2023.solutions.day01

import org.springframework.stereotype.Component

@Component
class InputProcessor01 {

    fun process(inputLines: List<String>): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        var calorieList = mutableListOf<Int>()
        inputLines.forEach { line ->
            if (line.isNotEmpty())
                calorieList.add(line.toInt())
            else {
                result.add(calorieList)
                calorieList = mutableListOf()
            }
        }
        result.add(calorieList)
        return result
    }
}