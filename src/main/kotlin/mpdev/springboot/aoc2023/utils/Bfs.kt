package mpdev.springboot.aoc2023.utils

import kotlin.math.min

class Bfs<T: Comparable<T>> {

    /**
     *  1  procedure BFS(G, root) is
     *  2      let Q be a queue
     *  3      label root as explored
     *  4      Q.enqueue(root)
     *  5      while Q is not empty do
     *  6          v := Q.dequeue()
     *  7          if v is the goal then
     *  8              return v
     *  9          for all edges from v to w in G.adjacentEdges(v) do
     * 10              if w is not labeled as explored then
     * 11                  label w as explored
     * 12                  w.parent := v
     * 13                  Q.enqueue(w)
     */

    /**
     * returns all paths from a to b
     */
    fun allPaths(a: Vertex<T>, b: Vertex<T>): List<List<Vertex<T>>> {
        val allPaths = mutableListOf<List<Vertex<T>>>()
        val queue = ArrayDeque<MutableList<Vertex<T>>>()
        var curPath = mutableListOf(a)
        queue.add(curPath)
        while (queue.isNotEmpty()) {
            curPath = queue.removeFirst()
            val lastNode = curPath.last()
            if (lastNode.getId() == b.getId())   // found path
                allPaths.add(curPath)
            else
                lastNode.getConnectedNodes().forEach { connectedNode ->
                    if (!curPath.contains(connectedNode)) {
                        val newPartialPath = curPath.toMutableList().also { it.add(connectedNode) }
                        queue.add(newPartialPath)
                    }
                }
        }
        return allPaths
    }

    /**
     * returns shortest path from a to b
     */
    fun shortestPath(a: Vertex<T>, b: Vertex<T>): List<Vertex<T>> {
        val queue = ArrayDeque<MutableList<Vertex<T>>>()
        var curPath = mutableListOf(a)
        val visited = mutableSetOf(a)
        queue.add(curPath)
        while (queue.isNotEmpty()) {
            curPath = queue.removeFirst()
            val lastNode = curPath.last()
            if (lastNode.getId() == b.getId())   // found path
                return curPath
            lastNode.getConnectedNodes().sortedBy { it.getId() }.forEach { connection ->
                if (!curPath.contains(connection) && !visited.contains(connection)) {
                    visited.add(connection)
                    val newPartialPath = curPath.toMutableList().also { it.add(connection) }
                    queue.add(newPartialPath)
                }
            }
        }
        throw AocException("no path found between [${a.getId()}] and [${b.getId()}]")
    }

    /**
     * traverses the graph and executes function f(T) for every node
     */
    fun traverseGraph(start: Vertex<T>, f: (T) -> Unit = {}) {
        val queue = ArrayDeque<Vertex<T>>().also { it.add(start) }
        val visited = mutableListOf<Vertex<T>>().also { it.add(start) }
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            f(current.getId())
            current.getConnectedNodes().forEach { connection ->
                if (!visited.contains(connection)) {
                    visited.add(connection)
                    queue.add(connection)
                }
            }
        }
    }

    /**
     * find the distances of all graph nodes from a start node
     */
    fun findMinDistanceOfAllNodesFromStart(start: Vertex<T>): Map<T,Int> {
        val distances = mutableMapOf(start.getId() to 0)
        val queue = ArrayDeque<Vertex<T>>().also { it.add(start) }
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            current.getConnectedNodes().forEach { connection ->
                distances[connection.getId()] = min(
                    distances[current.getId()]!! + 1, distances[connection.getId()] ?: Int.MAX_VALUE
                )
                queue.add(connection)
            }
        }
        return distances
    }

    /**
     * convert graph to String starting at node start
     */
    fun graphToString(graph: Graph<T>, start: Vertex<T>): String {
        return StringBuilder().also { s ->
            traverseGraph(start) { id ->
                s.append("node: $id, connects to:")
                    .also { s.append(graph[id].getConnectedNodes().map { n -> n.getId() }) }
                    .also { s.append("\n") }
            }
        }.toString()
    }
}