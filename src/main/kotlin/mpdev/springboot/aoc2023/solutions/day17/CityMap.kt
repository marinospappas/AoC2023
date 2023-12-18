package mpdev.springboot.aoc2023.solutions.day17

import mpdev.springboot.aoc2023.utils.Dijkstra
import mpdev.springboot.aoc2023.utils.DijkstraPathMap
import mpdev.springboot.aoc2023.utils.Graph
import mpdev.springboot.aoc2023.utils.GraphNode
import mpdev.springboot.aoc2023.utils.Grid
import mpdev.springboot.aoc2023.utils.MinCostPath
import mpdev.springboot.aoc2023.utils.Point

class CityMap(input: List<String>) {

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

    // TODO: refactor the below and move the constraint to getConnectedNodes in Vertex<T> in Graph.kt
    private fun pathConstraint(dijkstraPathMap: DijkstraPathMap<Point>,
                               currNode: Dijkstra.PathNode<Point>,
                               nextNode: Dijkstra.PathNode<Point>): Boolean {
        val nextPoint = nextNode.node?.getId()!!
        val curPath = dijkstraPathMap.getMinCostPath(currNode.node?.getId()!!, start).map { it.first } + nextPoint
        // check backtracking
        if (curPath.size > 2 && nextPoint == curPath[curPath.lastIndex-2]) {
            println("*** backtrack constraint: ${curPath.takeLast(15)}  not accepted")
            return false
        }
        val straightPath = getStraightPathSegment(curPath)
        //println("path $curPath -> straight segmt: $straightPath")
        // check min straight steps
        if (curPath.size >= minStraightSteps  &&  straightPath.size < minStraightSteps) {
         //   println("*** min straight path constraint: ${curPath.takeLast(15)} not accepted")
         //   return false
        }
        // check max straight steps
        if (straightPath.size - 1 > maxStraightSteps) {
            println("*** max straight path constraint: ${curPath.takeLast(15)} not accepted")
            return false
        }
        return true
    }

    private fun getStraightPathSegment(path: List<Point>): List<Point> {
        if (path.size < 2)
            return emptyList()
        val lastStep = path.last() - path[path.lastIndex - 1]
        val pathSegment = mutableListOf(path.last())
        for (i in path.lastIndex - 1 downTo 0)
            if (path[i + 1] - path[i] == lastStep)
                pathSegment.add(path[i])
            else
                break
        return pathSegment.reversed()
    }
}