package mpdev.springboot.aoc2023.solutions.day23

import mpdev.springboot.aoc2023.solutions.day23.HikePath.*
import mpdev.springboot.aoc2023.utils.*

class TrailMap(input: List<String>) {

    var debug = false
    val grid = Grid(input, HikePath.mapper, border = 0)
    val maxy = grid.getMinMaxXY().x4
    val start: Point = grid.getDataPoints().filter { e -> e.key.y == 0 && e.value == PATH }
        .keys.first()
    val end: Point = grid.getDataPoints().filter { e -> e.key.y == maxy && e.value == PATH }
        .keys.first()

    fun findMaxPath1(): Int {
        return initGraph(1).longestPathDfs(start, end)
    }

    fun findMaxPath2(): Int {
        return SGraph(reduceGraph(initGraph(2)).nodes).longestPathDfs(start, end)
    }

    /**
     * reduce the graph
     * by combining all the "straight" path segments to one connection
     * This is done by repeatedly taking all the nodes that have only 2 connected nodes (i.e. either right/left or up/down)
     * and taking these nodes out and connecting the node before with the node after and also adjusting the number of steps between
     * This way only nodes with 3 or 4 connections are left plus the start and the end
     */
    private fun reduceGraph(graph: SGraph<Point>): SGraph<Point> {
        val adjacencyMap = graph.nodes.toMutableMap()
        adjacencyMap.keys.toList().forEach { key ->
            adjacencyMap[key]?.takeIf { it.size == 2 }?.let { neighbors ->
                val left = neighbors.keys.first()
                val right = neighbors.keys.last()
                val totalSteps = neighbors[left]!! + neighbors[right]!!
                adjacencyMap.getOrPut(left) { mutableMapOf() }.merge(right, totalSteps, ::maxOf)
                adjacencyMap.getOrPut(right) { mutableMapOf() }.merge(left, totalSteps, ::maxOf)
                for (p in setOf(left,right)) adjacencyMap[p]?.remove(key)
                adjacencyMap.remove(key)
            }
        }
        return SGraph(adjacencyMap)
    }

    fun initGraph(part1or2: Int): SGraph<Point> {
        val g = SGraph<Point>()
        grid.getDataPoints().filter { e -> e.value != FORREST }.keys.forEach { p ->
            g.addNode(p, getAdjacent(p, part1or2))
        }
        return g
    }

    private fun getAdjacent(point: Point, part1Or2: Int): Set<Point> {
        val adjacent = mutableSetOf<Point>()
        val datum = grid.getDataPoint(point)
        if (part1Or2 == 1)
            when (grid.getDataPoint(point)) {
                PATH -> adjacent.addAll(point.adjacentCardinal())
                SLOPE_1,SLOPE_2,SLOPE_3,SLOPE_4 -> adjacent.add(point + GridUtils.Direction.of(datum!!.value).increment)
                else -> {}
            }
        else // part 2
            adjacent.addAll(point.adjacentCardinal())
        adjacent.removeAll { adj -> grid.getDataPoint(adj) == FORREST }
        if (point == start)
            adjacent.removeAll { it.y < 0 }
        else if (point == end)
            adjacent.clear()
        return adjacent
    }
}

enum class HikePath(val value: Char, val next: Point) {
    FORREST('#', Point(0,0)),
    PATH('.', Point(0,0)),
    SLOPE_1('^', Point(0,-1)),
    SLOPE_2('>', Point(1,0)),
    SLOPE_3('v', Point(0,1)),
    SLOPE_4('<', Point(-1,0));
    companion object {
        val mapper: Map<Char,HikePath> = values().associateBy { it.value }
    }
}
