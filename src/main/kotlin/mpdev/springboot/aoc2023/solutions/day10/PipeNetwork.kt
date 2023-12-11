package mpdev.springboot.aoc2023.solutions.day10

import mpdev.springboot.aoc2023.utils.GridUtils.Direction
import mpdev.springboot.aoc2023.utils.GridUtils.Direction.*
import mpdev.springboot.aoc2023.solutions.day10.LineSegment.*
import mpdev.springboot.aoc2023.utils.Grid
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
        when {
            direction == UP && grid.getDataPoint(p) == Vertical -> { nextPoint = p + Point(0,-1); nextDirection = UP }
            direction == UP && grid.getDataPoint(p) == TopRight -> { nextPoint = p + Point(-1,0); nextDirection = LEFT }
            direction == UP && grid.getDataPoint(p) == TopLeft -> { nextPoint = p + Point(1,0); nextDirection = RIGHT }

            direction == DOWN && grid.getDataPoint(p) == Vertical -> { nextPoint = p + Point(0,1); nextDirection = DOWN }
            direction == DOWN && grid.getDataPoint(p) == BottomRight -> { nextPoint = p + Point(-1,0); nextDirection = LEFT }
            direction == DOWN && grid.getDataPoint(p) == BottomLeft -> { nextPoint = p + Point(1,0); nextDirection = RIGHT }

            direction == LEFT && grid.getDataPoint(p) == Horizontal -> { nextPoint = p + Point(-1,0); nextDirection = LEFT }
            direction == LEFT && grid.getDataPoint(p) == BottomLeft -> { nextPoint = p + Point(0,-1); nextDirection = UP }
            direction == LEFT && grid.getDataPoint(p) == TopLeft -> { nextPoint = p + Point(0,1); nextDirection = DOWN }

            direction == RIGHT && grid.getDataPoint(p) == Horizontal -> { nextPoint = p + Point(1,0); nextDirection = RIGHT }
            direction == RIGHT && grid.getDataPoint(p) == BottomRight -> { nextPoint = p + Point(0,-1); nextDirection = UP }
            direction == RIGHT && grid.getDataPoint(p) == TopRight -> { nextPoint = p + Point(0,1); nextDirection = DOWN }
        }
        return if (grid.isInsideGrid(nextPoint)) Pair(nextPoint, nextDirection) else null
    }

    fun identifyStartDatum(): LineSegment {
        val valueBefore = loop.last().second
        val valueAfter = loop[1].second
        val pointBefore = loop.last().first
        val pointAfter = loop[1].first
        if (valueBefore == Vertical && setOf(Vertical,TopRight,TopLeft).contains(valueAfter))
            return Vertical
        if (valueAfter == Vertical && setOf(Vertical,BottomLeft,BottomRight).contains(valueBefore))
            return Vertical
        if (valueBefore == Horizontal && setOf(Vertical,TopRight,BottomRight).contains(valueAfter))
            return Horizontal
        if (valueAfter == Horizontal && setOf(Vertical,TopLeft,BottomLeft).contains(valueBefore))
            return Horizontal

        if (valueBefore == Vertical && valueAfter == Horizontal) {
            if (pointAfter.x > pointBefore.x)
                return TopLeft
            else
                return TopRight
        }
        if (valueBefore == Horizontal && valueAfter == Vertical) {
            if (pointAfter.x > pointBefore.x)
                return BottomRight
            else
                return TopLeft
        }
        return EMPTY
    }

    fun findPointsInsideLoop(): Int {
        loop[0] = Pair(loop[0].first, identifyStartDatum())
        val auxGrid = Grid(loop.associate { it.first to it.second }, LineSegment.mapper)
        val (minx, maxx, miny, maxy) = auxGrid.getMinMaxXY()
        var count = 0
        for (y in miny..maxy) {
            var countCrossings = 0
            for (x in minx..maxx) {
                val point = Point(x, y)
                if (listOf(Vertical, BottomLeft, BottomRight).contains(auxGrid.getDataPoint(point)))
                    ++countCrossings
                else
                    if (!loop.map { it.first }.contains(point) && countCrossings % 2 == 1)
                        ++count
            }
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
