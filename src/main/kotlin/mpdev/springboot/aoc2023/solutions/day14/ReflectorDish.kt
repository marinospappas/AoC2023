package mpdev.springboot.aoc2023.solutions.day14

import mpdev.springboot.aoc2023.utils.*
import mpdev.springboot.aoc2023.utils.GridUtils.Direction.*


class ReflectorDish(input: List<String>) {

    val grid = Grid(input, Rock.mapper, 0)

    fun calculateLoadUp(): Int {
        val maxy = grid.getMinMaxXY().x4
        return grid.getDataPoints().filter { it.value == Rock.SPHERE }.keys.sumOf { maxy + 1 - it.y }
    }

    fun calculateLoadForCycles(repeat: Int): Int {
        val (first, second, resultList) = findRepeatCycle()
        val cycle = second - first
        val remainder = (repeat - second) % cycle
        return resultList[first + remainder - 1]
    }

    private val MAX_CYCLE_TRIES = 400
    private val CYCLE_SETTLE_COUNT = 100

    fun findRepeatCycle(): Triple<Int,Int,List<Int>> {
        val resultsList = mutableListOf<Int>()
        for (i in 1 .. MAX_CYCLE_TRIES) {
            spinAll()
            val result = calculateLoadUp()
            if (i > CYCLE_SETTLE_COUNT && resultsList.contains(result))
                if (checkCycle(resultsList + result)) {
                    val first = resultsList.lastIndexOf(result)
                    resultsList.add(result)
                    return Triple(first + 1, resultsList.lastIndex + 1, resultsList)
                }
            resultsList.add(result)
        }
        return Triple(-1, -1, emptyList())
    }

    private fun checkCycle(list: List<Int>): Boolean {
        val indices = mutableListOf<Int>()
        for (i in list.indices)
            if (list[i] == list.last())
                indices.add(i)
        val cycle = indices[1] - indices[0]
        return (1 .. indices.lastIndex).all { indices[it] - indices[it-1] == cycle }
    }

    fun rollAllUp() {
        rollAll(grid.getDataPoints().filter { it.value == Rock.SPHERE }.keys.sorted())
    }

    fun spinAll() {
        var points = grid.getDataPoints().filter { it.value == Rock.SPHERE }.keys.sorted()
        rollAll(points, UP)
        points = grid.getDataPoints().filter { it.value == Rock.SPHERE }.keys.sortedWith(Point.ComparatorXY())
        rollAll(points, LEFT)
        points = grid.getDataPoints().filter { it.value == Rock.SPHERE }.keys.sorted().reversed()
        rollAll(points, DOWN)
        points = grid.getDataPoints().filter { it.value == Rock.SPHERE }.keys.sortedWith(Point.ComparatorXY()).reversed()
        rollAll(points, RIGHT)
    }

    private fun rollAll(points: List<Point>, direction: GridUtils.Direction = UP) {
        points.forEach { rollSphere(it, direction) }
    }

    private fun rollSphere(start: Point, direction: GridUtils.Direction = UP) {
        when (direction) {
            UP -> rollSphereUp(start)
            LEFT -> rollSphereLeft(start)
            DOWN -> rollSphereDown(start)
            RIGHT -> rollSphereRight(start)
        }
    }

    private fun rollSphereUp(start: Point) {
        val miny = grid.getMinMaxXY().x3
        var newY = start.y
        for (y in newY downTo miny + 1) {
            if (grid.getDataPoint(Point(start.x, y - 1)) != null)
                break
            --newY
        }
        if (newY != start.y) {
            grid.setDataPoint(Point(start.x, newY), grid.getDataPoint(start)!!)
            grid.removeDataPoint(start)
        }
    }

    private fun rollSphereLeft(start: Point) {
        val minx = grid.getMinMaxXY().x1
        var newX = start.x
        for (x in newX downTo minx + 1) {
            if (grid.getDataPoint(Point(x - 1, start.y)) != null)
                break
            --newX
        }
        if (newX != start.x) {
            grid.setDataPoint(Point(newX, start.y), grid.getDataPoint(start)!!)
            grid.removeDataPoint(start)
        }
    }

    private fun rollSphereDown(start: Point) {
        val maxy = grid.getMinMaxXY().x4
        var newY = start.y
        for (y in newY .. maxy - 1) {
            if (grid.getDataPoint(Point(start.x, y + 1)) != null)
                break
            ++newY
        }
        if (newY != start.y) {
            grid.setDataPoint(Point(start.x, newY), grid.getDataPoint(start)!!)
            grid.removeDataPoint(start)
        }
    }

    private fun rollSphereRight(start: Point) {
        val maxx = grid.getMinMaxXY().x2
        var newX = start.x
        for (x in newX .. maxx - 1) {
            if (grid.getDataPoint(Point(x + 1, start.y)) != null)
                break
            ++newX
        }
        if (newX != start.x) {
            grid.setDataPoint(Point(newX, start.y), grid.getDataPoint(start)!!)
            grid.removeDataPoint(start)
        }
    }
}

enum class Rock(val value: Char) {
    SPHERE('O'),
    CUBE('#');
    companion object {
        val mapper: Map<Char,Rock> = values().associateBy { it.value }
    }
}
