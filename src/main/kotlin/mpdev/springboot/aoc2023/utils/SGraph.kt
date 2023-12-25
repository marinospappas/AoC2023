package mpdev.springboot.aoc2023.utils

import java.util.HashSet

/**
 * simple graph class
 * nodes: map of node_id to (map connected_node_id to weight)
 */
//TODO replace the Int weight with Weight<U> to be able to implement custom compare
class SGraph<T>(var nodes: MutableMap<T, MutableMap<T, Int>> = mutableMapOf()) {

    constructor(nodesList: List<Pair<T, MutableSet<T>>>):
            this (nodesList.map { (id, conn) ->
                id to conn.associateWith { 1 }.toMutableMap()
            }.toMap(mutableMapOf()))

    operator fun get(id: T) = nodes[id] ?: throw AocException("SGraph: node $id not found")

    fun getOrNull(id: T) = nodes[id]

    fun getNodes() = nodes.keys.toList()

    fun getNodesAndConnections() = nodes.toList()

    fun removeConnection(edge: Set<T>) {
        nodes[edge.first()]!!.remove(edge.last())
        nodes[edge.last()]!!.remove(edge.first())
    }

    fun addNode(id: T, connected: Set<T>, connectBothWays: Boolean = false) {
        addNode(id, connected.associateWith { 1 }, connectBothWays)
    }

    fun addNode(id: T, connected: Map<T,Int> = mapOf(), connectBothWays: Boolean = false) {
        nodes.computeIfAbsent(id) { mutableMapOf() }
        connected.forEach { (k, v) -> nodes[id]!![k] = v }
        if (connectBothWays)
            connected.forEach { (cId, v) -> addNode(cId, mapOf(id to v)) }
    }

    fun getMaxPathDfs(start: T, end: T): Int {
        return dfsMaxPath(start, end, mutableMapOf()) ?: -1
    }

    //TODO: refactor the below function to use Stack instead of recursion
    private fun dfsMaxPath(cur: T, end: T, visited: MutableMap<T, Int>): Int? {
        if (cur == end) {
            return visited.values.sum()
        }
        var maxPath: Int? = null
        (nodes[cur] ?: emptyMap()).forEach { (neighbor, steps) ->
            if (neighbor !in visited) {
                visited[neighbor] = steps
                val res = dfsMaxPath(neighbor, end, visited)
                if (maxPath == null || (res != null && res > maxPath!!)) {
                    maxPath = res
                }
                visited.remove(neighbor)
            }
        }
        return maxPath
    }

}