package mpdev.springboot.aoc2023.utils

object GridUtils {

    /** draw a line from a starting point to one of the four directions, until it goes out of the grid
     *  and count how many occurrences of the items in set it finds
     */
    fun <T>drawLineAndCount(grid: Grid<T>, start: Point, direction: Direction, matchSet: Set<T>): Int {
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
    fun <T>isInsideArea(grid: Grid<T>, p: Point, polygonSegments: Set<T>) =
        drawLineAndCount(grid, p, Direction.LEFT, polygonSegments) % 2 != 0

    /**
     * fill an area of the grid that is surrounded by a set of points (assumed to be a closed irregular polygon)
     *
     */
    fun <T>floodArea(grid: Grid<T>, value: T, areaBorder: List<Point>, closedPolygonSegments: Set<T>) {

        var currentSet = areaBorder
        do {
            for (point in currentSet)
                grid.setDataPoint(point, value)
            currentSet = innerBorder(currentSet, grid, closedPolygonSegments)
        } while (currentSet.isNotEmpty())

        val visited = mutableSetOf<Point>()
        // for each point on the border find a point inside the polygon but not on the border
        for (p in areaBorder) {
            var startPoint = Point(-1, -1)
            for (point in p.adjacent()) {
                if (isInsideArea(grid, point, closedPolygonSegments) && !areaBorder.contains(point)) {
                    startPoint = point
                    break
                }
            }
            if (startPoint.x >= 0) {
                val queue = ArrayDeque<Point>().also { q -> q.add(startPoint) }
                while (queue.isNotEmpty()) {
                    val point = queue.removeFirst().also { visited.add(it) }
                    grid.setDataPoint(point, value)
                    point.adjacent()
                        .filter { isInsideArea(grid, it, closedPolygonSegments) && !areaBorder.contains(it) }
                        .forEach { adj ->
                            if (!visited.contains(adj))
                                queue.add(adj)
                        }
                }
            }
        }
    }

    private fun <T>innerBorder(border: List<Point>, grid: Grid<T>, closedPolygonSegments: Set<T>): List<Point> {
        val innerBrdr = mutableListOf<Point>()
        return innerBrdr
    }

    enum class Direction(val increment: Point) {
        UP(Point(0,-1)),
        RIGHT(Point(1,0)),
        DOWN(Point(0,1)),
        LEFT(Point(-1,0));
    }

}