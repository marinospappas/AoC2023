package mpdev.springboot.aoc2023.utils

object GridUtils {

    /** draw a line from a starting point to one of the four directions, until it goes out of the grid
     *  and count how many occurrences of the items in set it finds
     */
    fun <T> drawLineAndCount(grid: Grid<T>, start: Point, direction: Direction, matchSet: Set<T>): Int {
        var current = start
        var count = 0
        while (grid.isInsideGrid(current)) {
            if (matchSet.contains(grid.getDataPoint(current)))
                ++count
            current += direction.increment
        }
        return count
    }

    /**
     * find out if a point is inside an area described by a set of points (assumed to be a closed irregular polygon)
     * done by how many times the line crosses the polygon
     * to do this we need the values for Vertical, Bottom-Left and Bottom-Right
     */
    fun <T> isInsideArea(grid: Grid<T>, p: Point, polygonSegments: Set<T>) =
        drawLineAndCount(grid, p, Direction.LEFT, polygonSegments) % 2 != 0

    /**
     * fill an area of the grid that is surrounded by a set of points (assumed to be a closed irregular polygon)
     */
    fun <T> floodArea(grid: Grid<T>, start: Point, value: T) {
        val visited = mutableSetOf<Point>()
        val queue = ArrayDeque<Point>().also { q -> q.add(start) }
        while (queue.isNotEmpty()) {
            val point = queue.removeFirst().also { visited.add(it) }
            grid.setDataPoint(point, value)
            point.adjacentCardinal().forEach { adj ->
                if (!visited.contains(adj))
                    queue.add(adj)
            }
        }
    }

    /**
     * get gris perimeter
     */
    fun <T> getPerimeter(grid: Grid<T>): Set<Point> {
        val (minx, maxx, miny, maxy) = grid.getMinMaxXY()
        val perimeter = mutableSetOf<Point>()
        for (x in minx..maxx) {
            perimeter.add(Point(x,miny))
            perimeter.add(Point(x,maxy))
        }
        for (y in miny+1 .. maxy-1) {
            perimeter.add(Point(minx,y))
            perimeter.add(Point(maxx,y))
        }
        return perimeter
    }

    /**
     * example of walk grid function
     */
    /*
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
     */

    enum class Direction(val increment: Point) {
        UP(Point(0, -1)),
        RIGHT(Point(1, 0)),
        DOWN(Point(0, 1)),
        LEFT(Point(-1, 0));
        fun toString1() = when (this) { UP -> "U"; RIGHT -> "R"; DOWN -> "D"; LEFT -> "L" }
        fun toString2() = when (this) { UP -> "UP"; RIGHT -> "RIGHT"; DOWN -> "DOWN"; LEFT -> "LEFT" }
        fun toString3() = when (this) { UP -> "N"; RIGHT -> "E"; DOWN -> "S"; LEFT -> "W" }
        fun toString4() = when (this) { UP -> "NORTH"; RIGHT -> "EAST"; DOWN -> "SOUTH"; LEFT -> "WEST" }
        fun toString5() = when (this) { UP -> "^"; RIGHT -> ">"; DOWN -> "v"; LEFT -> "<" }
        companion object {
            fun of(s: String): Direction =
                when (s) {
                    "U","UP",   "N","NORTH","^" -> UP
                    "R","RIGHT","E","EAST", ">" -> RIGHT
                    "D","DOWN", "S","SOUTH","v" -> DOWN
                    "L","LEFT", "W","WEST", "<" -> LEFT
                    else -> throw AocException("invalid Direction: [$s]")
                }
            fun of(c: Char): Direction = of(c.toString())
            fun of(inc: Point): Direction =
                values().firstOrNull { it.increment == inc } ?: throw AocException("invalid Direction increment: [$inc]")
            fun oppositeOf(d: Direction) = when (d) { UP -> DOWN; RIGHT -> LEFT; DOWN -> UP; LEFT -> RIGHT }
        }
    }
}