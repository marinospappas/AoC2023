package mpdev.springboot.aoc2023.solutions.day03

import mpdev.springboot.aoc2023.utils.Grid
import mpdev.springboot.aoc2023.utils.Point

class Engine(input: List<String>) {

    val grid = Grid(input, Grid.allCharsDefMapper, 0)

    fun findSumOfNumsAdjToSymbol(): Int {
        var result = 0
        var current = advanceToNextNumber(grid.firstPoint())
        while (grid.isInsideGrid(current)) {
            val n = getNextNumber(current)
            if (isAdjacentToSymbol(n.second))
                result += n.first.toInt()
            current = advanceToNextNumber(grid.nextPoint(current + Point(n.first.length,0)))
        }
        return result
    }

    fun findSumOfGearBoxRatios(): Int {
        val gearMap = mutableMapOf<Point, MutableList<Int>>()
        var current = advanceToNextNumber(grid.firstPoint())
        while (grid.isInsideGrid(current)) {
            val n = getNextNumber(current)
            isAdjacentToGear(n.second).let {
                if (it.first)
                    it.second.forEach { gear ->
                        gearMap.getOrPut(gear) { mutableListOf() }.add(n.first.toInt())
                    }
            }
            current = advanceToNextNumber(grid.nextPoint(current+ Point(n.first.length,0)))
        }
        return gearMap.values.filter { it.size == 2 }.sumOf { it.reduce(Int::times) }
    }

    // return the next number as a string and also the set of points that this number lives in
    fun getNextNumber(p: Point): Pair<String,Set<Point>> {
        var resNum = ""
        val resPts = mutableSetOf<Point>()
        var current = p
        current = advanceToNextNumber(current)
        while (grid.isInsideGrid(current)) {
            val datum = grid.getDataPoint(current) ?: break
            if (!datum.isDigit())
                break
            resNum += datum.toString()
            resPts.add(current)
            current += Point(1,0)
        }
        return Pair(resNum,resPts)
    }

    // advances the point to the next number in the grid
    fun advanceToNextNumber(p: Point): Point {
        var current = p
        while (grid.isInsideGrid(current) && !grid.getDataPoint(current).isDigit()) {
            current = grid.nextPoint(current)
        }
        return current
    }

    // all the points that are adjacent to a set of points
    fun getAllAdjacent(points: Set<Point>) =
        points.map { it.adjacent().toList() }.flatten().toSet() - points

    // true if the set points has a symbol adjacent
    fun isAdjacentToSymbol(points: Set<Point>) =
        getAllAdjacent(points).any { grid.getDataPoint(it).isSymbol() }

    // return true if a gear (or more) is adjacent to any point in the set and also the position of the gear(s)
    fun isAdjacentToGear(points: Set<Point>): Pair<Boolean,Set<Point>> {
        getAllAdjacent(points).filter { p -> grid.getDataPoint(p) == GEAR }.toSet().let {
            return Pair(it.isNotEmpty(), it)
        }
    }

    private fun Char?.isDigit() = if (this == null) false else this in '0'..'9'
    private fun Char?.isSymbol() = if (this == null) false
        else setOf('+','@','#','%','/','$','-','=','&','*').contains(this)

    companion object {
        const val GEAR = '*'
    }
}
