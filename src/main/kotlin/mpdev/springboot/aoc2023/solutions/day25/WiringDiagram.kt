package mpdev.springboot.aoc2023.solutions.day25

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Serializable
@AocInClass(delimiters = [": "])
data class AoCInput(
    // cmg: qnr nvd lhk bvb
    // 0    1
    @AocInField(0) val id: String,
    @AocInField(1, delimiters = [" "]) val connections: List<String>
)

class WiringDiagram(val input: List<String>) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)
    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    val graph = SGraph<String>()

    init {
        aocInputList.forEach { inputRec ->
            graph.addNode(inputRec.id, inputRec.connections.toSet(), connectBothWays = true)
        }
    }

    /**
     * solution 1 - too slow
     * For each connected pair
     *   break the connection and get the shortest path between that pair
     *   for each component in the above path
     *     break that connection and get the shortest path between that latest pair
     *     for each component in the latest path
     *       break that connection and get the shortest path between that latest pair
     *       if there is no path between them then we have the three connections to break
     */
    fun breakConnectionsV1(): Pair<Int,Int> {
        val totalGraphCount = graph.getAllConnectedNodes(graph.nodes.keys.first()).size
        for (connPair1 in graph.getAllConnectedPairs()) {
            val tmpGraph1 = SGraph.of(graph).also { g -> g.removeConnection(connPair1) }
            val pathAB1 = tmpGraph1.shortestPathBfs(connPair1.first(), connPair1.last())
            val connectedPairs2 = mutableSetOf<Set<String>>()
            for (i in 0 .. pathAB1.lastIndex - 1)
                for (j in i + 1 .. pathAB1.lastIndex)
                    connectedPairs2.add(setOf(pathAB1[i], pathAB1[j]))
            for (connPair2 in connectedPairs2) {
                val tmpGraph2 = SGraph.of(tmpGraph1).also { g -> g.removeConnection(connPair2) }
                val pathAB2 = tmpGraph2.shortestPathBfs(connPair1.first(), connPair1.last())
                val connectedPairs3 = mutableSetOf<Set<String>>()
                for (i in 0 .. pathAB2.lastIndex - 1)
                    for (j in i + 1 .. pathAB2.lastIndex)
                        connectedPairs3.add(setOf(pathAB2[i], pathAB2[j]))
                for (connPair3 in connectedPairs3) {
                    val tmpGraph3 = SGraph.of(tmpGraph2).also { g -> g.removeConnection(connPair3) }
                    val pathAB3 = tmpGraph3.shortestPathBfs(connPair1.first(), connPair1.last())
                    if (pathAB3 == listOf<String>()) {
                        log.info("removed connections: {}, {}, {}", connPair1, connPair2, connPair3)
                        val count1 = tmpGraph3.getAllConnectedNodes(connPair1.first()).size
                        val count2 = tmpGraph3.getAllConnectedNodes(connPair1.last()).size
                        if (count1 + count2 == totalGraphCount)
                            return Pair(count1, count2)
                    }
                }
            }
        }
        return Pair(-1,-1)
    }

    /**
     * solution 2 - fast
     *  For each connected pair
     *    break the connection and get the shortest path between that pair
     *  Take the three pairs that are furthest apart (the three longest paths)
     *    these are the three connections to break
     */
    fun breakConnectionsV2(): Pair<Int,Int> {
        val totalGraphCount = graph.getAllConnectedNodes(graph.nodes.keys.first()).size
        val pathSizes = mutableMapOf<Set<String>,Int>()
        for (connPair in graph.getAllConnectedPairs()) {
            val tmpGraph1 = SGraph.of(graph)
            tmpGraph1.removeConnection(connPair)
            pathSizes[connPair] = tmpGraph1.shortestPathBfs(connPair.first(), connPair.last()).size
        }
        val connectionsToRemove = pathSizes.entries.sortedBy { it.value }.reversed().take(3).map { it.key }
        connectionsToRemove.forEach { graph.removeConnection(it) }
        log.info("removed connections: {}", connectionsToRemove)
        val count1 = graph.getAllConnectedNodes(connectionsToRemove.first().first()).size
        val count2 = graph.getAllConnectedNodes(connectionsToRemove.first().last()).size
        if (count1 + count2 == totalGraphCount)
            return Pair(count1, count2)
        return Pair(-1,-1)
    }
}