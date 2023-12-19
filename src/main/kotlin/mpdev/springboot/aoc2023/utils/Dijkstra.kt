package mpdev.springboot.aoc2023.utils

import java.util.*

/**
 * Dijkstra implementation
 * T is the type of the Node ID in the Graph
 * the costMap can be provided by the caller, or else all costs default to 1
 */
class Dijkstra<T>(private var costMap: Map<Pair<T,T>,Int>? = null) {

    data class PathNode<T>(
        val node: Vertex<T>?,
        var costFromStart: Int,
        var updatedBy: T? = null): Comparable<PathNode<T>> {
        override fun compareTo(other: PathNode<T>): Int {
            // in Dijkstra min cost criteria is based on cost from start to this node
            return costFromStart.compareTo(other.costFromStart)
        }
    }

    class DijkstraException(override var message: String): Exception()

    private fun getCost(from: T, to: T) =
        if (costMap == null) 1      // default cost is 1 for all moves if no cost map given
        else costMap!![Pair(from, to)] ?: throw DijkstraException("cost from $from to $to not defined")

    private lateinit var currentNode: PathNode<T>

    /**
     * The Dijkstra algorithm implementation
     * start: Vertex, end: Vertex
     */
    fun runIt(startState: Vertex<T>, endState: Vertex<T>, maxPath: Int = Int.MAX_VALUE,
              pathConstraint: (DijkstraPathMap<T>, PathNode<T>, PathNode<T>) -> Boolean = {_,_,_ -> true}) =
        runIt(startState, { id -> id == endState.getId() }, maxPath, pathConstraint )

    /**
     * The Dijkstra algorithm implementation
     * start: Vertex, end: function that returns true when we reach the end
     * pathConstraint is a function that will decide whether a connected node from the list of connections
     * can be added to the path (defaults to true)
     */
    fun runIt(startState: Vertex<T>, isAtEnd: (T) -> Boolean, maxPath: Int = Int.MAX_VALUE,
              pathConstraint: (DijkstraPathMap<T>, PathNode<T>, PathNode<T>) -> Boolean = {_,_,_ -> true}): MinCostPath<T> {
        // setup priority queue, visited set and minimum total costs for each node
        val toVisitQueue = PriorityQueue<PathNode<T>>().apply { add(PathNode(startState, 0)) }
        val visitedNodes = mutableSetOf<PathNode<T>>()
        val dijkstraCost = DijkstraPathMap<T>()

        var iterations = 0
        // while the priority Q has elements, get the top one (least cost as per Comparator)
        while (toVisitQueue.isNotEmpty()) {
            currentNode = toVisitQueue.poll()
            // if this is the endNode ID, we are done
            if (isAtEnd(currentNode.node?.getId()!!)) {
                val minCostPath = MinCostPath<T>()
                minCostPath.minCost = currentNode.costFromStart
                minCostPath.numberOfIterations = iterations
                minCostPath.path = dijkstraCost.getMinCostPath(currentNode.node!!.getId(), startState.getId())
                if (minCostPath.path.size > maxPath)
                    throw DijkstraException("Calculated Path exceeded max size ($maxPath)")
                return minCostPath
            }
            // else for each connected node
            currentNode.node!!.getConnectedNodes(dijkstraCost).forEach { connectedNode ->
                val nextPathNode = PathNode(connectedNode, getCost(currentNode.node!!.getId(), connectedNode.getId()))
                if (!pathConstraint(dijkstraCost, currentNode, nextPathNode) || visitedNodes.contains(nextPathNode))
                    return@forEach
                ++iterations
                visitedNodes.add(nextPathNode)
                // calculate the new cost to that node and the new *estimated* total cost to the end node
                val newCost = currentNode.costFromStart + getCost(currentNode.node!!.getId(),connectedNode.getId())
                // if the new cost is less than what we have already recorded in the map of nodes/costs
                // update the map with the new costs and "updatedBy" (to be able to back-track the min.cost path)
                if (newCost < dijkstraCost.pathMap.getValue(connectedNode.getId()).costFromStart) {
                    nextPathNode.updatedBy = currentNode.node!!.getId()
                    nextPathNode.costFromStart = newCost
                    dijkstraCost.pathMap[connectedNode.getId()] = nextPathNode
                    // and put the updated new node back into the priority queue
                    toVisitQueue.add(nextPathNode)
                }
            }
        }
        throw DijkstraException("no path found from ${startState.getId()} to endState")
    }
}

data class DijkstraPathMap<T>(val pathMap: MutableMap<T, Dijkstra.PathNode<T>> =
    mutableMapOf<T, Dijkstra.PathNode<T>>().withDefault { Dijkstra.PathNode(null, Int.MAX_VALUE) }) {
    fun getMinCostPath(minCostKey: T, startKey: T): List<Pair<T,Int>> {
        var key = minCostKey
        val path = mutableListOf<Pair<T,Int>>()
        do {
            val node = pathMap.getValue(key)
            path.add(Pair(key,node.costFromStart))
            key = node.updatedBy ?: startKey
        } while (key != startKey)
        path.add(Pair(key, 0))
        return path.reversed()
    }
}