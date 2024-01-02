package mpdev.springboot.aoc2023.utils

import java.util.*

/**
 * The A* algorithm - improves the search by adding estimated cost to end
 */
inline fun <reified T> SGraph<T>.aStar(from: T, isAtEnd: (T) -> Boolean):  MinCostPath<T> {
    val priorityQueue = PriorityQueue<SGraphPathNode<T>>().apply { add(SGraphPathNode(from, 0)) }
    val visited = mutableSetOf<SGraphPathNode<T>>()
    val astarCost = SGraphPathMap<T>()
    var currentNode: SGraphPathNode<T>

    SGraph.aStarAlgorithm = true   // ensure dijkstra is used
    var iterations = 0
    // while the priority Q has elements, get the top one (least cost as per Comparator)
    while (priorityQueue.isNotEmpty()) {
        currentNode = priorityQueue.poll()
        // if this is the endNode ID, we are done
        if (isAtEnd(currentNode.id!!)) {
            val minCostPath = MinCostPath<T>()
            minCostPath.minCost = currentNode.costFromStart
            minCostPath.numberOfIterations = iterations
            minCostPath.path = astarCost.getMinCostPath(currentNode?.id!!, from)
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
            val newTotalCost = newCost + (heuristic(connectedNode.first) ?:
                                throw SGraphException("A* algorithm cannot be run as the heuristic function is not defined"))
            // if the new cost is less than what we have already recorded in the map of nodes/costs
            // update the map with the new costs and "updatedBy" (to be able to back-track the min.cost path)
            if (newCost < astarCost.pathMap.getValue(connectedNode.first).costFromStart) {
                nextPathNode.updatedBy = currentNode.id!!
                nextPathNode.costFromStart = newCost
                nextPathNode.estTotalCostToEnd = newTotalCost
                astarCost.pathMap[connectedNode.first] = nextPathNode
                // and put the updated new node back into the priority queue
                priorityQueue.add(nextPathNode)
            }
        }
    }
    astarCost.pathMap.forEach { it.println() }
    throw SGraphException("no path found from $from to endState")
}
