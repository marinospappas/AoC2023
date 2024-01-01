package mpdev.springboot.aoc2023.utils

import java.util.*

inline fun <reified T> SGraph<T>.dijkstra(from: T, isAtEnd: (T) -> Boolean):  MinCostPath<T> {
    val priorityQueue = PriorityQueue<SGraphPathNode<T>>().apply { add(SGraphPathNode(from, 0)) }
    val visited = mutableSetOf<SGraphPathNode<T>>()
    val dijkstraCost = SGraphPathMap<T>()
    var currentNode: SGraphPathNode<T>

    var iterations = 0
    // while the priority Q has elements, get the top one (least cost as per Comparator)
    while (priorityQueue.isNotEmpty()) {
        currentNode = priorityQueue.poll()
        // if this is the endNode ID, we are done
        if (isAtEnd(currentNode.id!!)) {
            val minCostPath = MinCostPath<T>()
            minCostPath.minCost = currentNode.costFromStart
            minCostPath.numberOfIterations = iterations
            minCostPath.path = dijkstraCost.getMinCostPath(currentNode?.id!!, from)
            return minCostPath
        }
        // else for each connected node
       getConnected(currentNode.id!!).forEach { connectedNode ->
            val nextPathNode = SGraphPathNode(connectedNode.first, connectedNode.second)
            if (visited.contains(nextPathNode))
                return@forEach
            ++iterations
            visited.add(nextPathNode)
            // calculate the new cost to that node and the new *estimated* total cost to the end node
            val newCost = currentNode.costFromStart + connectedNode.second
            // if the new cost is less than what we have already recorded in the map of nodes/costs
            // update the map with the new costs and "updatedBy" (to be able to back-track the min.cost path)
            if (newCost < dijkstraCost.pathMap.getValue(connectedNode.first).costFromStart) {
                nextPathNode.updatedBy = currentNode.id!!
                nextPathNode.costFromStart = newCost
                dijkstraCost.pathMap[connectedNode.first] = nextPathNode
                // and put the updated new node back into the priority queue
                priorityQueue.add(nextPathNode)
            }
        }
    }
    dijkstraCost.pathMap.forEach { it.println() }
    throw SGraphException("no path found from $from to endState")

}

data class SGraphPathNode<T>(val id: T?, var costFromStart: Int, var updatedBy: T? = null):
    Comparable<SGraphPathNode<T>> {
    override fun compareTo(other: SGraphPathNode<T>): Int {
        // in Dijkstra min cost criteria is based on cost from start to this node
        return costFromStart.compareTo(other.costFromStart)
    }
}

data class SGraphPathMap<T>(val pathMap: MutableMap<T, SGraphPathNode<T>> =
    mutableMapOf<T, SGraphPathNode<T>>().withDefault { SGraphPathNode(null, Int.MAX_VALUE) }) {
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

class SGraphException(override var message: String): Exception()