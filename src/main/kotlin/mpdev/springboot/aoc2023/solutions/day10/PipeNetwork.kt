package mpdev.springboot.aoc2023.solutions.day10

import mpdev.springboot.aoc2023.utils.GridUtils.Direction
import mpdev.springboot.aoc2023.utils.GridUtils.Direction.*
import mpdev.springboot.aoc2023.solutions.day10.LineSegment.*
import mpdev.springboot.aoc2023.utils.Grid
import mpdev.springboot.aoc2023.utils.GridUtils.isInsideArea
import mpdev.springboot.aoc2023.utils.Point

class PipeNetwork(input: List<String>) {

    val grid = Grid(input, LineSegment.mapper, border = 0)
    val start = grid.getDataPoints().entries.first { it.value == START }.key

    val loop = mutableListOf<Pair<Point,LineSegment>>()

    fun findLoop() {
        val adjacent = setOf(
            Pair(start + Point(0,-1), UP),
            Pair(start + Point(1,0), RIGHT),
            Pair(start + Point(0,1), DOWN),
            Pair(start + Point(-1,0), LEFT)
        )
        adjacent.forEach { adj ->
            if (isLoop(start, adj.first, adj.second)) {
                return
            }
        }
    }

    fun isLoop(p: Point, next: Point, direction: Direction): Boolean {
        var current = next
        var currDirection = direction
        loop.clear()
        loop.add(Pair(start, grid.getDataPoint(start)!!))
        repeat(grid.getDimensions().first * grid.getDimensions().second) {
            if (current == p)
                return true
            loop.add(Pair(current, grid.getDataPoint(current)!!))
            val nextPointInLoop = walkGrid(current, currDirection) ?: return false
            current = nextPointInLoop.first
            currDirection = nextPointInLoop.second
        }
        return false
    }

    // TODO: move walkGrid to GridUtils
    fun walkGrid(p: Point, direction: Direction): Pair<Point,Direction>? {
        if (!grid.isInsideGrid(p))
            return null
        var nextPoint: Point = p
        var nextDirection: Direction = direction
        when (direction) {
            UP -> {
                when (grid.getDataPoint(p)) {
                    Vertical -> { nextPoint = p + Point(0,-1) }
                    TopRight -> { nextPoint = p + Point(-1,0); nextDirection = LEFT }
                    TopLeft -> { nextPoint = p + Point(1,0); nextDirection = RIGHT }
                    else -> return null
                }
            }
            DOWN -> {
                when (grid.getDataPoint(p)) {
                    Vertical -> { nextPoint = p + Point(0,1) }
                    BottomRight -> { nextPoint = p + Point(-1,0); nextDirection = LEFT }
                    BottomLeft -> { nextPoint = p + Point(1,0); nextDirection = RIGHT }
                    else -> return null
                }
            }
            LEFT -> {
                when (grid.getDataPoint(p)) {
                    Horizontal -> { nextPoint = p + Point(-1,0) }
                    BottomLeft -> { nextPoint = p + Point(0,-1); nextDirection = UP }
                    TopLeft -> { nextPoint = p + Point(0,1); nextDirection = DOWN }
                    else -> return null
                }
            }
            RIGHT -> {
                when (grid.getDataPoint(p)) {
                    Horizontal -> { nextPoint = p + Point(1,0) }
                    BottomRight -> { nextPoint = p + Point(0,-1); nextDirection = UP }
                    TopRight -> { nextPoint = p + Point(0,1); nextDirection = DOWN }
                    else -> return null
                }
            }
        }
        return if (grid.isInsideGrid(nextPoint)) Pair(nextPoint, nextDirection) else null
    }

    fun identifyStartDatum(): LineSegment = Vertical

    fun findPointsInsideLoop(): Int {
        loop[0] = Pair(loop[0].first, identifyStartDatum())
        val auxGrid = Grid(loop.associate { it.first to it.second }, LineSegment.mapper)
        val (minx, maxx, miny, maxy) = auxGrid.getMinMaxXY()
        var count = 0
        for (x in minx..maxx)
            for (y in miny .. maxy) {
                if (loop.map { it.first }.contains(Point(x,y)))
                    continue
                if(isInsideArea(auxGrid,Point(x,y), setOf(Vertical, BottomLeft, BottomRight)))
                    ++count
            }
        return count
    }
}

enum class LineSegment(val value: Char) {
    Vertical('|'),
    Horizontal('-'),
    BottomLeft('L'),
    BottomRight('J'),
    TopRight('7'),
    TopLeft('F'),
    START('S'),
    LOOP('*'),
    INSIDE('I'),
    OUTSIDE('O'),
    EMPTY('.');
    companion object {
        val mapper: Map<Char,LineSegment> = values().associateBy { it.value }
    }
}
