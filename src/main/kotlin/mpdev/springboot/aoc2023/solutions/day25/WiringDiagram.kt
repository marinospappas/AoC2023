package mpdev.springboot.aoc2023.solutions.day25

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.*
import java.util.*
import kotlin.Comparator

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
    //val graph_: MutableMap<String, MutableSet<String>> = mutableMapOf()
    val graph = SGraph<String>()

    init {
        aocInputList.forEach { inputRec ->
            graph.addNode(inputRec.id, inputRec.connections.toSet(), connectBothWays = true)


            /*
            for (conxn in inputRec.connections) {
                graph_.computeIfAbsent(inputRec.id) { _ -> HashSet() }.add(conxn)
                graph_.computeIfAbsent(conxn) { _ -> HashSet() }.add(inputRec.id)
            }

             */
        }
    }

    fun solvePart1(): Int {
        val edgeFrequency: MutableMap<Set<String>, Int> = mutableMapOf()
        val vertices: List<String> = graph.getNodes()
        for (i in 0 .. vertices.lastIndex - 1) {
            val start = vertices[i]
            for (j in i + 1 .. vertices.lastIndex) {
                val target = vertices[j]
                markEdges(start, target, edgeFrequency)
            }
        }
        edgeFrequency.entries.stream()
            .sorted(Comparator.comparingInt { (_, value): Map.Entry<Set<String>, Int> -> value }
                .reversed()).limit(3).forEach { (keyPair): Map.Entry<Set<String>, Int> ->
                graph.removeConnection(keyPair)
            }
        val part1Size = findConnectedSize(graph.getNodes().first())
        return part1Size * (graph.getNodes().size - part1Size)
    }

    /*
    fun solvePart1_(): Int {
        val edgeFrequency: MutableMap<Set<String>, Int> = mutableMapOf()
        val vertexes: List<String> = graph_.keys.toList()
        for (i in vertexes.indices) {
            val start = vertexes[i]
            for (j in i + 1 until vertexes.size) {
                val target = vertexes[j]
                markEdges_(start, target, graph_, edgeFrequency)
            }
        }
        edgeFrequency.entries.stream()
            .sorted(Comparator.comparingInt { (_, value): Map.Entry<Set<String>, Int> -> value }
                .reversed()).limit(3).forEach { (key): Map.Entry<Set<String>, Int> ->
                cutEdge(graph_, key)
            }
        val part1Size = findConnectedSize_(graph_.keys.iterator().next(), graph_)
        return part1Size * (graph_.size - part1Size)
    }

     */

    private fun markEdges(
        start: String, target: String, edgeFrequency: MutableMap<Set<String>, Int>) {
        val queue: Queue<Step> = LinkedList()
        val visited: MutableSet<String> = mutableSetOf()
        queue.add(Step(start, listOf()))
        visited.add(start)
        while (!queue.isEmpty()) {
            val curr = queue.poll()
            if (curr.vertex == target) {
                curr.edges.forEach { e: Set<String> ->
                    val v = edgeFrequency.getOrDefault(e, 0)
                    edgeFrequency[e] = v + 1
                }
                return
            }
            graph[curr.vertex].filter { n -> !visited.contains(n.key) }.forEach { n ->
                val nextEdges: MutableList<Set<String>> =
                    curr.edges.toMutableList()
                nextEdges.add(setOf(curr.vertex, n.key))
                val nextStep = Step(n.key, nextEdges)
                queue.add(nextStep)
                visited.add(n.key)
            }
        }
    }

    /*
    private fun markEdges_(
        start: String, target: String, graph: Map<String, MutableSet<String>>,
        edgeFrequency: MutableMap<Set<String>, Int>) {
        val queue: Queue<Step> = LinkedList()
        val visited: MutableSet<String> = mutableSetOf()
        queue.add(Step(start, listOf()))
        visited.add(start)
        while (!queue.isEmpty()) {
            val curr = queue.poll()
            if (target == curr.vertex) {
                curr.edges.forEach { e: Set<String> ->
                    val v = edgeFrequency.getOrDefault(e, 0)
                    edgeFrequency[e] = v + 1
                }
                return
            }
            graph[curr.vertex]!!.stream().filter { n: String -> !visited.contains(n) }.forEach { n: String ->
                val nextEdges: MutableList<Set<String>> =
                    curr.edges.toMutableList()
                nextEdges.add(setOf(curr.vertex, n))
                val nextStep = Step(n, nextEdges)
                queue.add(nextStep)
                visited.add(n)
            }
        }
    }

    private fun cutEdge(graph: Map<String, MutableSet<String>>, edge: Set<String>) {
        val it = edge.iterator()
        val a = it.next()
        val b = it.next()
        graph[a]!!.remove(b)
        graph[b]!!.remove(a)
    }

     */

    private fun findConnectedSize(start: String): Int {
        val queue: Queue<String> = LinkedList()
        val visited: MutableSet<String> = mutableSetOf()
        queue.add(start)
        visited.add(start)
        while (!queue.isEmpty()) {
            val curr = queue.poll()
            graph[curr].filter { n -> !visited.contains(n.key) }.forEach { n ->
                queue.add(n.key)
                visited.add(n.key)
            }
        }
        return visited.size
    }

    /*
    private fun findConnectedSize_(start: String, graph: Map<String, MutableSet<String>>): Int {
        val queue: Queue<String> = LinkedList()
        val visited: MutableSet<String> = mutableSetOf()
        queue.add(start)
        visited.add(start)
        while (!queue.isEmpty()) {
            val curr = queue.poll()
            graph[curr]!!.stream().filter { n: String -> !visited.contains(n) }.forEach { n: String ->
                queue.add(n)
                visited.add(n)
            }
        }
        return visited.size
    }
     */

    internal class Step(var vertex: String, val edges: List<Set<String>>)
}