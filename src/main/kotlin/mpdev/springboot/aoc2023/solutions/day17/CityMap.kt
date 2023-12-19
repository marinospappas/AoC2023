package mpdev.springboot.aoc2023.solutions.day17

import mpdev.springboot.aoc2023.utils.*
import mpdev.springboot.aoc2023.utils.GridUtils.Direction.*

class CityMap(input: List<String>) {

    val grid = Grid(input, ('0'..'9').associateWith { it.digitToInt() }, border=0)
    private val graph = Graph(this::getNeighbours)
    private val start = Point(0,0)

    init {
        grid.getDataPoints().forEach { graph.addNode(it.key) }
        grid.getDataPoints().forEach { e ->
            e.key.adjacentCardinal().filter { grid.isInsideGrid(it) }.forEach { p ->
                // only update cost - connected nodes will be calculated dynamically based on constraints
                graph.updateCost(e.key, p, grid.getDataPoint(p)!!)
            }
        }
    }

    var maxStraightSteps = 4
    var minStraightSteps = 1

    fun getNeighbours(p: Point, dijkstraPathMap: DijkstraPathMap<Point>?): List<GraphNode<Point>> {
        var neighbours = p.adjacentCardinal().toList()
        val curPath = dijkstraPathMap?.getMinCostPath(p, start)?.map { it.first }!!
        if (curPath.size < 2 || curPath[0] == curPath[1])
            return neighbours.filter { grid.isInsideGrid(it) }.map { graph[it] }
        val lastStep = curPath.last() - curPath[curPath.lastIndex - 1]
        val direction = GridUtils.Direction.of(lastStep)
        val straightSegment = getStraightPathSegment(curPath, lastStep).size
        if (straightSegment == maxStraightSteps)
            neighbours = when (direction) {
                UP -> listOf(p + LEFT.increment, p + RIGHT.increment)
                RIGHT -> listOf(p + UP.increment, p + DOWN.increment)
                DOWN -> listOf(p + LEFT.increment, p + RIGHT.increment)
                LEFT -> listOf(p + UP.increment, p + DOWN.increment)
            }
        return neighbours.filter { grid.isInsideGrid(it) }.map { graph[it] }
    }

    fun findMinPath():MinCostPath<Point> {
        val (_, maxx, _, maxy) = grid.getMinMaxXY()
        val startNode = graph[start]
        val endNode = graph[Point(maxx,maxy)]
        return Dijkstra(graph.costs).runIt(startNode, endNode)
    }



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
        val lastStep = curPath.last() - curPath[curPath.lastIndex - 1]
        val straightPath = getStraightPathSegment(curPath, lastStep)
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

    private fun getStraightPathSegment(path: List<Point>, lastStep: Point): List<Point> {
        if (path.size < 2)
            return emptyList()
        val pathSegment = mutableListOf(path.last())
        for (i in path.lastIndex - 1 downTo 0)
            if (path[i + 1] - path[i] == lastStep)
                pathSegment.add(path[i])
            else
                break
        return pathSegment.reversed()
    }
}