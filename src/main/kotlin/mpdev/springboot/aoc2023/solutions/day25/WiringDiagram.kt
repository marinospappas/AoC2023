package mpdev.springboot.aoc2023.solutions.day25

import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap
import kotlin.collections.HashSet

// TODO: review the below to see if performance can be improved - use the SGraph class
class WiringDiagram(val input: List<String>) {

    val graph: MutableMap<String, MutableSet<String>> = HashMap()

    fun solvePart1(): Int {
        input.forEach { l: String -> processLine(l, graph) }
        val edgeFrequency: MutableMap<Set<String>, Int> = mutableMapOf()
        val vertexes: List<String> = graph.keys.toList()
        for (i in vertexes.indices) {
            val start = vertexes[i]
            for (j in i + 1 until vertexes.size) {
                val target = vertexes[j]
                markEdges(start, target, graph, edgeFrequency)
            }
        }
        edgeFrequency.entries.stream()
            .sorted(Comparator.comparingInt { (_, value): Map.Entry<Set<String>, Int> -> value }
                .reversed()).limit(3).forEach { (key): Map.Entry<Set<String>, Int> ->
                cutEdge(graph, key)
            }
        val part1Size = findConnectedSize(graph.keys.iterator().next(), graph)
        return part1Size * (graph.size - part1Size)
    }

    private fun markEdges(
        start: String, target: String, graph: Map<String, MutableSet<String>>,
        edgeFrequency: MutableMap<Set<String>, Int>
    ) {
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

    private fun findConnectedSize(start: String, graph: Map<String, MutableSet<String>>): Int {
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

    private fun processLine(line: String, graph: MutableMap<String, MutableSet<String>>) {
        val parts = line.split(": ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val name = parts[0]
        val cons = parts[1].split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (con in cons) {
            markConnection(graph, name, con)
        }
    }

    private fun markConnection(graph: MutableMap<String, MutableSet<String>>, from: String, to: String) {
        graph.computeIfAbsent(from) { k: String? -> HashSet() }.add(to)
        graph.computeIfAbsent(to) { k: String? -> HashSet() }.add(from)
    }

    internal class Step(var vertex: String, val edges: List<Set<String>>)
}