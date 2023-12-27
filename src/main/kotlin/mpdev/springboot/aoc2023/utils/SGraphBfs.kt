package mpdev.springboot.aoc2023.utils

inline fun <reified T: Comparable<T>> SGraph<T>.shortestPathBfs(from: T, to: T): List<T> {
    var curPath = mutableListOf(from)
    val visited = mutableSetOf(from)
    val queue = ArrayDeque<MutableList<T>>().also { l -> l.add(curPath) }
    while (queue.isNotEmpty()) {
        curPath = queue.removeFirst()
        val lastNode = curPath.last()
        if (lastNode == to)   // found path
            return curPath
        getConnected(lastNode).map { it.first }.sortedBy { it }.forEach { connection ->
            if (!curPath.contains(connection) && !visited.contains(connection)) {
                visited.add(connection)
                val newPartialPath = curPath.toMutableList().also { it.add(connection) }
                queue.add(newPartialPath)
            }
        }
    }
    return emptyList()
}

inline fun <reified T: Comparable<T>> SGraph<T>.allPaths(from: T, to: T): List<Set<T>> {
    val allPaths = mutableListOf<Set<T>>()
    val queue = ArrayDeque<MutableSet<T>>()
    var curPath = mutableSetOf(from)
    queue.add(curPath)
    while (queue.isNotEmpty()) {
        curPath = queue.removeFirst()
        val lastNode = curPath.last()
        if (lastNode == to)   // found path
            allPaths.add(curPath)
        else
            getConnected(lastNode).map { it.first }.forEach { connectedNode ->
                if (!curPath.contains(connectedNode)) {
                    val newPartialPath = curPath.toMutableSet().also { it.add(connectedNode) }
                    queue.add(newPartialPath)
                }
            }
    }
    return allPaths
}

inline fun <reified T: Comparable<T>> SGraph<T>.getAllConnectedNodes(from: T): Set<T> {
    val visited = mutableSetOf(from)
    val queue = ArrayDeque<T>().also { l -> l.add(from) }
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        getConnected(current).map { it.first }.sortedBy { it }.forEach { connection ->
            if (!visited.contains(connection)) {
                visited.add(connection)
                queue.add(connection)
            }
        }
    }
    return visited
}