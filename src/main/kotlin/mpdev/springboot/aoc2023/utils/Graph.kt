package mpdev.springboot.aoc2023.utils

class Graph<T>(var getConnections: (id: T) -> List<GraphNode<T>>? = { null } ) {

    private val nodes = mutableMapOf<T, GraphNode<T>>()

    val costs = mutableMapOf<Pair<T,T>,Int>()
    val heuristics = mutableMapOf<T,Int>()

    operator fun get(id: T) = nodes[id] ?:
        throw AocException("node id [${id}] not found")

    fun getOrNull(id: T) = nodes[id]

    fun nodeExists(id: T) = nodes[id] != null

    fun addNode(id: T) {
        nodes[id] = GraphNode(id) { nodeId -> getConnections(nodeId) }
    }

    fun connect(first: T, second: T) = connect(this[first], this[second])

    fun connectBothWays(first: T, second: T) {
        connect(this[first], this[second])
        connect(this[second], this[first])
    }

    private fun connect(first: GraphNode<T>, second: GraphNode<T>) {
        if(!first.neighbours.contains(second))
            first.neighbours.add(second)
    }

    fun updateHeuristic(id: T, heuristic: Int) {
        heuristics[id] = heuristic
    }

    fun updateCost(fromId: T, toId: T, cost: Int) {
        costs[Pair(fromId,toId)] = cost
    }

    fun getNodes() = nodes

    private fun getNeighbours(id: T) = nodes[id]?.getConnectedNodes()

    /*override fun toString(): String {
        return StringBuilder().also { s ->
            nodes.keys.forEach { id ->
                s.append("id: $id, connects to:").also {
                    getNeighbours(id)?.forEach { n -> s.append(" ${n.getId()}, cost: ${costs[Pair(id, n.getId())]}")
                        .append(", heur: ${heuristics[id]}") }
                }.also { s.append("\n") }
            }
        }.toString()
    }*/
}

class GraphNode<T>(var nodeId: T, var getConnections: (id: T) -> List<GraphNode<T>>? = { null }): Vertex<T> {

    val neighbours = mutableListOf<GraphNode<T>>()

    override fun getId() = nodeId

    override fun setId(id: T) {
        nodeId = id
    }

    override fun getConnectedNodes() = getConnections(nodeId) ?: neighbours
}

interface Vertex<T> {
    fun getId(): T
    fun setId(id: T)
    fun getConnectedNodes(): List<Vertex<T>>
}

class MinCostPath<T> {
    var path: List<Pair<T,Int>> = listOf()
    var minCost: Int = Int.MAX_VALUE
    var numberOfIterations: Int = 0
}