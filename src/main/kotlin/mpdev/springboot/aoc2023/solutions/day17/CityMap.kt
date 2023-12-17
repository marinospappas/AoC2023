package mpdev.springboot.aoc2023.solutions.day17

import mpdev.springboot.aoc2023.utils.Dijkstra
import mpdev.springboot.aoc2023.utils.DijkstraPathMap
import mpdev.springboot.aoc2023.utils.Graph
import mpdev.springboot.aoc2023.utils.GraphNode
import mpdev.springboot.aoc2023.utils.Grid
import mpdev.springboot.aoc2023.utils.MinCostPath
import mpdev.springboot.aoc2023.utils.Point
import kotlin.math.min


class CityMap(input: List<String>) {

    var debug = false
    val grid = Grid(input, ('0'..'9').associateWith { it.digitToInt() }, border=0)
    private val graph = Graph(this::getNeighbours)
    private val start = Point(0,0)

    init {
        grid.getDataPoints().forEach { graph.addNode(it.key) }
        grid.getDataPoints().forEach { e ->
            e.key.adjacentCardinal().filter { grid.isInsideGrid(it) }.forEach { p ->
                graph.connect(e.key, p)
                graph.updateCost(e.key, p, grid.getDataPoint(p)!!)
            }
        }
    }

    fun getNeighbours(p: Point): List<GraphNode<Point>> {
        val neighbours = p.adjacentCardinal().filter { grid.isInsideGrid(it) }
        return neighbours.map { graph[it] }
    }

    fun findMinPath():MinCostPath<Point> {
        val (_, maxx, _, maxy) = grid.getMinMaxXY()
        val startNode = graph[start]
        val endNode = graph[Point(maxx,maxy)]
        return Dijkstra(graph.costs).runIt(startNode, endNode,
            pathConstraint = { pathMap, currNode, nextNode -> pathConstraint(pathMap, currNode, nextNode) })
    }

    var maxStraightSteps = 3
    var minStraightSteps = 1

    fun pathConstraint(dijkstraPathMap: DijkstraPathMap<Point>,
                       currNode: Dijkstra.PathNode<Point>,
                       nextNode: Dijkstra.PathNode<Point>): Boolean {
        val curPath = dijkstraPathMap.getMinCostPath(currNode.node?.getId()!!, start)
        if (curPath.size > 1 && nextNode.node?.getId() == curPath[curPath.lastIndex-1].first)   // path cannot backtrack
            return false
        val nextPoint = nextNode.node?.getId()!!
        // check min straight steps
        val lastPathPointsMin = curPath.takeLast(min(curPath.size, minStraightSteps - 1)).map { it.first }
        if (!lastPathPointsMin.all { it.x == nextPoint.x } && !lastPathPointsMin.all { it.y == nextPoint.y }) {
            //println("not accepted")
            return false
        }
        // check max straight steps
        if (curPath.size > maxStraightSteps) {
            val lastPathPoints = curPath.takeLast(maxStraightSteps+1).map { it.first }
            println("$lastPathPoints, $nextPoint")
            if (lastPathPoints.all { it.x == nextPoint.x } || lastPathPoints.all { it.y == nextPoint.y }) {
                //println("not accepted")
                return false
            }
        }
        return true
    }
}