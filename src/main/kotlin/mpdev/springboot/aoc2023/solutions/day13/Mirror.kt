package mpdev.springboot.aoc2023.solutions.day13

import mpdev.springboot.aoc2023.utils.Grid
import mpdev.springboot.aoc2023.solutions.day13.ReflectionType.*
import kotlin.math.abs
import kotlin.math.min


class Mirror(input: List<String>) {

    var reflections: List<Reflection> = processInput(input)

    fun checkReflection(obj: Reflection, comparator: (List<Int>, List<Int>) -> Boolean) {
        val grid = obj.grid
        val (minx, maxx, miny, maxy) = grid.getMinMaxXY()
        var reflLine: Int
        // vertical
        if ((minx..maxx).map { grid.mapColToInt(it) }.isReflected(comparator).also { reflLine = it } > 0) {
            obj.reflType = V
            obj.reflLine = reflLine
            return
        }
        // horizontal
        if ((miny..maxy).map { grid.mapRowToInt(it) }.isReflected(comparator).also { reflLine = it } > 0) {
            obj.reflType = H
            obj.reflLine = reflLine
        }
    }

    private fun List<Int>.isReflected(compare: (List<Int>, List<Int>) -> Boolean): Int {
        for (reflLine in this.lastIndex downTo 1) {
            val size = min(reflLine, this.size - reflLine)
            val (list1, list2) =
                if (reflLine <= this.size / 2)
                    Pair(this.subList(0, reflLine), this.subList(reflLine, reflLine + size))
                else
                    Pair(this.subList(reflLine - size, reflLine), this.subList(reflLine, this.size))
            if (compare(list1, list2))
                return reflLine
        }
        return 0
    }

    // part 1 comparison - straight mirror image
    fun listCompare1(list1: List<Int>, list2: List<Int>) = list1 == list2.reversed()

    // part 2 comparison - mirror image must differ by only 1 bit
    fun listCompare2(list1: List<Int>, list2: List<Int>): Boolean {
        val list2r = list2.reversed()
        val countEq = list1.indices.count { list1[it] == list2r[it] }
        val countOffBy1Bit = list1.indices.count { abs(list1[it] - list2r[it]).isPowerOf2() }
        return countEq == list1.size - 1 && countOffBy1Bit == 1
    }

    fun Int.isPowerOf2() = this.toString(2).count { it == '1' } == 1

    ////////////////////////////////////////////////////////////

    private fun processInput(input: List<String>): List<Reflection> {
        val reflList = mutableListOf<Reflection>()
        val gridData = mutableListOf<String>()
        input.forEach { line ->
            if (line.isEmpty()) {
                reflList.add(Reflection(Grid(gridData, ReflectionDatum.mapper, 0)))
                gridData.clear()
                return@forEach
            }
            gridData.add(line)
        }
        reflList.add(Reflection(Grid(gridData, ReflectionDatum.mapper, 0)))
        return reflList
    }
}

data class Reflection(val grid: Grid<ReflectionDatum>, var reflType: ReflectionType = NONE,
                      var reflLine: Int = -1) {
    fun print() {
        grid.print()
        println("Reflection: $reflType at $reflLine\n")
    }
}

enum class ReflectionType{ H, V, NONE }

enum class ReflectionDatum(val value: Char) {
    ROCK('#');
    companion object {
        val mapper: Map<Char,ReflectionDatum> = values().associateBy { it.value }
    }
}