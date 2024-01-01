package mpdev.springboot.aoc2023.utils

/**
 * simple graph class
 * nodes: map of node_id to (map connected_node_id to weight)
 */
//TODO replace the Int weight with Weight<U> to be able to implement custom compare
class SGraph<T>(var nodes: MutableMap<T, MutableMap<T, Int>> = mutableMapOf(),
    val getConnected: (T) -> Set<Pair<T,Int>> = {id -> (nodes[id] ?: emptyMap()).map { Pair(it.key, it.value) }.toSet()}) {

    constructor(nodesList: List<Pair<T, MutableSet<T>>>):
            this (nodesList.map { (id, conn) ->
                id to conn.associateWith { 1 }.toMutableMap()
            }.toMap(mutableMapOf()))

    operator fun get(id: T) = nodes[id] ?: throw AocException("SGraph: node $id not found")

    fun getOrNull(id: T) = nodes[id]

    fun getNodes() = nodes.keys.toList()

    fun getNodesAndConnections() = nodes.toList()

    fun addNode(id: T, connected: T, connectBothWays: Boolean = false) {
        addNode(id, mapOf(connected to 1), connectBothWays)
    }

    fun addNode(id: T, connected: Set<T> = emptySet(), connectBothWays: Boolean = false) {
        addNode(id, connected.associateWith { 1 }, connectBothWays)
    }

    fun addNode(id: T, connected: Map<T,Int> = mapOf(), connectBothWays: Boolean = false) {
        nodes.computeIfAbsent(id) { mutableMapOf() }
        connected.forEach { (k, v) -> nodes[id]!![k] = v }
        if (connectBothWays)
            connected.forEach { (cId, v) -> addNode(cId, mapOf(id to v)) }
    }

    fun getAllConnectedPairs(): Set<Set<T>> {
        return nodes.map { n -> n.value.map { c -> setOf(n.key, c.key) } }.flatten().toSet()
    }

    fun removeConnection(a: T, b: T) {
        removeConnection(setOf(a, b))
    }

    fun getCost(a:T, b:T) = nodes[a]?.get(b)!!

    fun removeConnection(edge: Set<T>) {
        nodes[edge.first()]?.remove(edge.last())
        nodes[edge.last()]?.remove(edge.first())
    }

    fun longestPathDfs(start: T, end: T): Int {
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

    companion object {
        inline fun <reified T: Comparable<T>> of(g: SGraph<T>): SGraph<T> {
            val newGraph = SGraph<T>()
            for ((id, connxns) in g.nodes) {
                for ((connId, cost) in connxns)
                    newGraph.addNode(id, mapOf(connId to cost))
            }
            return newGraph
        }
    }
}