package mpdev.springboot.aoc2023.solutions.day10

import mpdev.springboot.aoc2023.utils.GridUtils.Direction
import mpdev.springboot.aoc2023.utils.GridUtils.Direction.*
import mpdev.springboot.aoc2023.solutions.day10.Segment.*
import mpdev.springboot.aoc2023.utils.Grid
import mpdev.springboot.aoc2023.utils.GridUtils
import mpdev.springboot.aoc2023.utils.GridUtils.isInsideArea
import mpdev.springboot.aoc2023.utils.Point

class PipeNetwork(input: List<String>) {

    val grid = Grid(input, Segment.mapper, border = 0)
    val start = grid.getDataPoints().entries.filter { it.value == START }.first().key
    val origGrid = Grid(grid.getDataPoints(), Segment.mapper, border = 0)

    val loop = mutableListOf<Pair<Point,Segment>>()

    fun findLoop() {
        val adjacent = setOf(
            Pair(start + Point(0,-1), UP),
            Pair(start + Point(1,0), RIGHT),
            Pair(start + Point(0,1), DOWN),
            Pair(start + Point(-1,0), LEFT)
        )
        adjacent.forEach { adj ->
            println("checking ${start}, ${adj}")
            if (isLoop(start, adj.first, adj.second)) {
                println("${start}, ${adj} is loop")
                return
            }
            println("no loop")
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
            val next = walkGrid(current, currDirection) ?: return false
            current = next.first
            currDirection = next.second
        }
        return false
    }

    fun walkGrid(p: Point, direction: Direction): Pair<Point,Direction>? {
        if (!grid.isInsideGrid(p))
            return null
        var nextPoint: Point = p
        var nextDirection: Direction = direction
        when (direction) {
            UP -> {
                when (grid.getDataPoint(p)) {
                    VERT -> { nextPoint = p + Point(0,-1) }
                    NTOW -> { nextPoint = p + Point(-1,0); nextDirection = LEFT }
                    NTOE -> { nextPoint = p + Point(1,0); nextDirection = RIGHT }
                    else -> return null
                }
            }
            DOWN -> {
                when (grid.getDataPoint(p)) {
                    VERT -> { nextPoint = p + Point(0,1) }
                    STOW -> { nextPoint = p + Point(-1,0); nextDirection = LEFT }
                    STOE -> { nextPoint = p + Point(1,0); nextDirection = RIGHT }
                    else -> return null
                }
            }
            LEFT -> {
                when (grid.getDataPoint(p)) {
                    HORIZ -> { nextPoint = p + Point(-1,0) }
                    STOE -> { nextPoint = p + Point(0,-1); nextDirection = UP }
                    NTOE -> { nextPoint = p + Point(0,1); nextDirection = DOWN }
                    else -> return null
                }
            }
            RIGHT -> {
                when (grid.getDataPoint(p)) {
                    HORIZ -> { nextPoint = p + Point(1,0) }
                    STOW -> { nextPoint = p + Point(0,-1); nextDirection = UP }
                    NTOW -> { nextPoint = p + Point(0,1); nextDirection = DOWN }
                    else -> return null
                }
            }
        }
        return if (grid.isInsideGrid(nextPoint)) Pair(nextPoint, nextDirection) else null
    }

    fun identifyStartDatum(loop: List<Point>): Segment = VERT

    fun findPointsInsideLoop(): Int {
        val auxGrid = Grid(loop.associate { it.first to it.second }, Segment.mapper)
        auxGrid.print()
        //GridUtils.floodArea(auxGrid, INSIDE, loop.map { it.first }, setOf(VERT, STOE, STOW))
        return auxGrid.getDataPoints()
            .filter { auxGrid.getDataPoint(it.key) == EMPTY }
            .count { e -> isInsideArea(auxGrid, e.key, setOf(VERT, STOE, STOW)) }
    }

    /*
    fun markLoop() {
        for (p in loop)
            grid.setDataPoint(p, LOOP)
    }

    fun removeJunk() {
        for (p in grid.getDataPoints().filter { !loop.contains(it.key) }.keys)
            grid.setDataPoint(p, EMPTY)
    }

    fun markOutside() {
        val (minx, maxx, miny, maxy) = grid.getMinMaxXY()
        for (y in miny..maxy) {
            for (x in minx..maxx) {
                if (grid.getDataPoint(Point(x, y)) != LOOP)
                    grid.setDataPoint(Point(x, y), OUTSIDE)
                else
                    break
            }
            for (x in maxx downTo minx) {
                if (grid.getDataPoint(Point(x, y)) != LOOP)
                    grid.setDataPoint(Point(x, y), OUTSIDE)
                else
                    break
            }
        }
        for (y in setOf(miny, maxy))
            for (x in minx..maxx)
                if (grid.getDataPoint(Point(x, y)) != LOOP)
                    grid.setDataPoint(Point(x, y), OUTSIDE)
        for (x in setOf(minx, maxx))
            for (y in miny..maxy)
                if (grid.getDataPoint(Point(x, y)) != LOOP)
                    grid.setDataPoint(Point(x, y), OUTSIDE)
    }

    fun markAdjacentOutside() {
        val (minx, maxx, miny, maxy) = grid.getMinMaxXY()
        for (y in miny .. maxy) {
            for (x in minx .. maxx) {
                if (grid.getDataPoint(Point(x,y)) != EMPTY) continue
                if (Point(x,y).adjacent(diagonally = false).any { grid.getDataPoint(it) == OUTSIDE })
                    grid.setDataPoint(Point(x,y), OUTSIDE)
            }
            for (x in maxx downTo minx) {
                if (grid.getDataPoint(Point(x,y)) != EMPTY) continue
                if (Point(x,y).adjacent(diagonally = false).any { grid.getDataPoint(it) == OUTSIDE })
                    grid.setDataPoint(Point(x,y), OUTSIDE)
            }
        }
        for (y in maxy downTo miny) {
            for (x in minx .. maxx) {
                if (grid.getDataPoint(Point(x,y)) != EMPTY) continue
                if (Point(x,y).adjacent(diagonally = false).any { grid.getDataPoint(it) == OUTSIDE })
                    grid.setDataPoint(Point(x,y), OUTSIDE)
            }
            for (x in maxx downTo minx) {
                if (grid.getDataPoint(Point(x,y)) != EMPTY) continue
                if (Point(x,y).adjacent(diagonally = false).any { grid.getDataPoint(it) == OUTSIDE })
                    grid.setDataPoint(Point(x,y), OUTSIDE)
            }
        }
    }


    fun markAdjacentInside() {
        val (minx, maxx, miny, maxy) = grid.getMinMaxXY()
        for (y in miny .. maxy) {
            for (x in minx .. maxx) {
                if (grid.getDataPoint(Point(x,y)) != EMPTY) continue
                if (Point(x,y).adjacent(diagonally = false).any { grid.getDataPoint(it) == INSIDE })
                    grid.setDataPoint(Point(x,y), INSIDE)
            }
            for (x in maxx downTo minx) {
                if (grid.getDataPoint(Point(x,y)) != EMPTY) continue
                if (Point(x,y).adjacent(diagonally = false).any { grid.getDataPoint(it) == INSIDE })
                    grid.setDataPoint(Point(x,y), INSIDE)
            }
        }
        for (y in maxy downTo miny) {
            for (x in minx .. maxx) {
                if (grid.getDataPoint(Point(x,y)) != EMPTY) continue
                if (Point(x,y).adjacent(diagonally = false).any { grid.getDataPoint(it) == INSIDE })
                    grid.setDataPoint(Point(x,y), INSIDE)
            }
            for (x in maxx downTo minx) {
                if (grid.getDataPoint(Point(x,y)) != EMPTY) continue
                if (Point(x,y).adjacent(diagonally = false).any { grid.getDataPoint(it) == INSIDE })
                    grid.setDataPoint(Point(x,y), INSIDE)
            }
        }
    }



    fun markInsideLoop() {
        val (minx, maxx, miny, maxy) = grid.getMinMaxXY()
        for (y in miny .. maxy) {
            for (x in minx..maxx) {
                if (grid.getDataPoint(Point(x, y)) != EMPTY
                    || x < loop.minOf { it.x } || x > loop.maxOf { it.x }
                    || y < loop.minOf { it.y } || y > loop.maxOf { it.y }
                    ) continue
                if (isInsideLoop(Point(x,y)))
                    grid.setDataPoint(Point(x, y), INSIDE)
            }
        }
    }
    */

}

enum class Segment(val value: Char) {
    /*
    | is a vertical pipe connecting north and south.
- is a horizontal pipe connecting east and west.
L is a 90-degree bend connecting north and east.
J is a 90-degree bend connecting north and west.
7 is a 90-degree bend connecting south and west.
F is a 90-degree bend connecting south and east.
. is ground; there is no pipe in this tile.
S is the starting position of the animal;
there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.
     */
    VERT('|'),
    HORIZ('-'),
    STOE('L'),
    STOW('J'),
    NTOW('7'),
    NTOE('F'),
    START('S'),
    LOOP('*'),
    INSIDE('I'),
    OUTSIDE('O'),
    EMPTY('.');
     companion object {
        val mapper: Map<Char,Segment> = values().associateBy { it.value }
    }
}

enum class LineSegment(val value: Char) {
    Vertical('|'),
    Horizontal('-'),
    BottomLeft('L'),
    BottomRight('J'),
    TopRight('7'),
    TopLeft('F'),
    EMPTY('.');
    companion object {
        val mapper: Map<Char,LineSegment> = values().associateBy { it.value }
    }
}
