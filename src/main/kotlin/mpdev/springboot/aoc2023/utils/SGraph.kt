package mpdev.springboot.aoc2023.utils

import java.util.*

/**
 * simple graph class
 * nodes: map of node_id to (map connected_node_id to weight)
 */
//TODO replace the Int weight with Weight<U> to be able to implement custom compare
class SGraph<T>(var nodes: MutableMap<T, MutableMap<T, Int>> = mutableMapOf()) {

    fun get(id: T) = nodes[id] ?: throw AocException("SGraph: node $id not found")

    fun getOrNull(id: T) = nodes[id]

    fun addNode(node: T, connected: Map<T,Int> = mapOf()) {
        nodes[node] = connected.toMutableMap()
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