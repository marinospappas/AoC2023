package mpdev.springboot.aoc2023.utils

import java.util.*

class Dfs<T> {

    /**
     * procedure DFS(G, v) is
     *     let S be a stack
     *     S.push(v)
     *     while S is not empty do
     *         v = S.pop()
     *         if v is not labeled as discovered then
     *             label v as discovered
     *             for all edges from v to w in G.adjacentEdges(v) do
     *                 S.push(w)
     */

    /**
     * returns all paths from a to b
     */
    fun allPaths(a: Vertex<T>, b: Vertex<T>): List<List<Vertex<T>>> {
        val allPaths = mutableListOf<List<Vertex<T>>>()
        var curPath = mutableListOf<Vertex<T>>().also { it.add(a) }
        val visited = mutableListOf<List<Vertex<T>>>()
        val stack = Stack<MutableList<Vertex<T>>>().also { it.push(curPath) }
        while (stack.isNotEmpty()) {
            curPath = stack.pop()
            val lastNode = curPath.last()
            if (!visited.contains(curPath)) {
                visited.add(curPath)
                if (lastNode.getId() == b.getId())   // found path
                    allPaths.add(curPath)
                else
                    lastNode.getConnectedNodes().forEach { connectedNode ->
                        val newPartialPath = curPath.toMutableList().also { it.add(connectedNode) }
                        stack.push(newPartialPath)
                    }
            }
        }
        return allPaths
    }

    /**
     * traverses the graph and executes function f(T) for every node
     */
    fun traverseGraph(start: Vertex<T>, f: (T) -> Unit = {}) {
        val stack = Stack<Vertex<T>>().also { it.push(start) }
        val visited = mutableListOf<Vertex<T>>()
        while (stack.isNotEmpty()) {
            val current = stack.pop()
            f(current.getId())
            if (!visited.contains(current)) {
                visited.add(current)
                current.getConnectedNodes().forEach { connection ->
                    stack.push(connection)
                }
            }
        }
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