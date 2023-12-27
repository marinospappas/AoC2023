package mpdev.springboot.aoc2023.solutions.day23

import mpdev.springboot.aoc2023.solutions.day23.HikePath.*
import mpdev.springboot.aoc2023.utils.*

// TODO: refactor this solution to use the new SGraph class and DFS also for part 1 for consistency and efficiency
class TrailMap(input: List<String>) {

    var debug = false
    val grid = Grid(input, HikePath.mapper, border = 0)
    val maxy = grid.getMinMaxXY().x4
    val start: Point = grid.getDataPoints().filter { e -> e.key.y == 0 && e.value == PATH }
        .keys.first()
    val end: Point = grid.getDataPoints().filter { e -> e.key.y == maxy && e.value == PATH }
        .keys.first()

    private val graph = Graph(this::getNextPathPts).also { g -> grid.getDataPoints().keys.forEach { g.addNode(it) } }

    private fun getNextPathPts(current: Point, ignore: DijkstraPathMap<Point>?): List<GraphNode<Point>> {
        val nextPts = mutableListOf<Point>()
        val curPathPt = grid.getDataPoint(current)!!
        if (curPathPt in SLOPE_1 .. SLOPE_4) {
            nextPts.add(current + curPathPt.next)
        }
        else {
            nextPts.addAll(
                current.adjacentCardinal().filter { grid.getDataPoint(it) != null && grid.getDataPoint(it)!! in PATH .. SLOPE_4 }
            )
        }
        return nextPts.map { graph[it] }
    }

    fun findAllPaths(): List<List<Point>> {
        val allPaths = Bfs<Point>().allPaths(graph[start], graph[end])
        return allPaths.map { l -> l.map { v -> v.getId() } }
    }

    fun findMaxPath1(): Int {
        return initGraph(1).longestPathDfs(start, end)
    }

    fun findMaxPath2(): Int {
        return SGraph(reduceGraph(initGraph(2)).nodes).longestPathDfs(start, end)
    }

    fun reduceGraph(graph: SGraph<Point>): SGraph<Point> {
        val adjacencyMap = graph.nodes.toMutableMap()
        adjacencyMap.keys.toList().forEach { key ->
            adjacencyMap[key]?.takeIf { it.size == 2 }?.let { neighbors ->
                val left = neighbors.keys.first()
                val right = neighbors.keys.last()
                val totalSteps = neighbors[left]!! + neighbors[right]!!
                adjacencyMap.getOrPut(left) { mutableMapOf() }.merge(right, totalSteps, ::maxOf)
                adjacencyMap.getOrPut(right) { mutableMapOf() }.merge(left, totalSteps, ::maxOf)
                listOf(left, right).forEach { adjacencyMap[it]?.remove(key) }
                adjacencyMap.remove(key)
            }
        }
        return SGraph(adjacencyMap)
    }

    fun initGraph(part1or2: Int): SGraph<Point> {
        val g = SGraph<Point>()
        if (part1or2 == 1)
            grid.getDataPoints().filter { e -> e.value == PATH }.keys.forEach { p ->
                val adjacent =
                    p.adjacentCardinal().filter { adj ->
                        grid.isInsideGrid(adj) && grid.getDataPoint(adj) == PATH
                    }
                g.addNode(p, adjacent.associateWith { 1 })
            }
        else
            grid.getDataPoints().filter { e -> e.value != FORREST }.keys.forEach { p ->
                val adjacent =
                    p.adjacentCardinal().filter { adj ->
                        grid.isInsideGrid(adj) && grid.getDataPoint(adj) != FORREST
                    }
                g.addNode(p, adjacent.associateWith { 1 })
            }
        return g
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
