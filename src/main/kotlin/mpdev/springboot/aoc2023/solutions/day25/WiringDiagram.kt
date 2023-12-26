package mpdev.springboot.aoc2023.solutions.day25

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.*
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayDeque

@Serializable
@AocInClass(delimiters = [": "])
data class AoCInput(
    // cmg: qnr nvd lhk bvb
    // 0    1
    @AocInField(0) val id: String,
    @AocInField(1, delimiters = [" "]) val connections: List<String>
)

// TODO: review the below to see if performance can be improved
class WiringDiagram(val input: List<String>) {

    var debug = false
    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    val graph = SGraph<String>()

    init {
        aocInputList.forEach { inputRec ->
            graph.addNode(inputRec.id, inputRec.connections.toSet(), connectBothWays = true)
        }
    }

    fun solvePart1(): Int {
        val connFrequency: MutableMap<Set<String>, Int> = mutableMapOf()
        val nodes: List<String> = graph.getNodes()
        for (i in 0 .. nodes.lastIndex - 1) {
            for (j in i + 1 .. nodes.lastIndex)
                markConnections(nodes[i], nodes[j], connFrequency)
        }
        connFrequency.entries
            .sortedBy { e -> e.value }.reversed()
            .take(3).forEach { (keyPair): Map.Entry<Set<String>, Int> ->
                graph.removeConnection(keyPair)
            }
        val part1Size = getGroupSize(graph.getNodes().first())
        return part1Size * (graph.getNodes().size - part1Size)
    }

    private fun markConnections(
        start: String, end: String, connectionFrequency: MutableMap<Set<String>, Int>) {
        val queue = ArrayDeque<Step>()
        val visited = mutableSetOf<String>()
        queue.add(Step(start, listOf()))
        visited.add(start)
        while (!queue.isEmpty()) {
            val curr = queue.removeFirst()
            if (curr.nodeId == end) {
                curr.connections.forEach { e ->
                    val v = connectionFrequency.getOrDefault(e, 0)
                    connectionFrequency[e] = v + 1
                }
                return
            }
            graph[curr.nodeId].keys.filter { n -> !visited.contains(n) }.forEach { n ->
                val nextConnections: MutableList<Set<String>> =
                    curr.connections.toMutableList()
                nextConnections.add(setOf(curr.nodeId, n))
                val nextStep = Step(n, nextConnections)
                queue.add(nextStep)
                visited.add(n)
            }
        }
    }

    private fun getGroupSize(start: String): Int {
        val queue: Queue<String> = LinkedList()
        val visited: MutableSet<String> = mutableSetOf()
        queue.add(start)
        visited.add(start)
        while (!queue.isEmpty()) {
            val curr = queue.poll()
            graph[curr].keys.filter { n -> !visited.contains(n) }.forEach { n ->
                queue.add(n)
                visited.add(n)
            }
        }
        return visited.size
    }

    internal class Step(var nodeId: String, val connections: List<Set<String>>)
}