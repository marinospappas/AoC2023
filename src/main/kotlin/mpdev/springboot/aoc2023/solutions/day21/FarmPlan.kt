package mpdev.springboot.aoc2023.solutions.day21

import mpdev.springboot.aoc2023.utils.*

class FarmPlan(input: List<String>) {

    val grid = Grid(input, FarmPlot.mapper, border=0)
    val start = grid.getDataPoints().filter { it.value == FarmPlot.START }.keys.firstOrNull()
        ?: throw AocException("no starting point")

    fun traverseGrid(start: Point, toLevel: Int): List<Point> {
        val border = mutableListOf<Point>()
        val queue = ArrayDeque<Pair<Point,Int>>().also { it.add(Pair(start,0)) }
        val visited = mutableListOf<Pair<Point,Int>>().also { it.add(Pair(start,0)) }
        while (queue.isNotEmpty() && queue.any { it.second <= toLevel }) {
            val (currentPoint, currentLevel) = queue.removeFirst()
            val neighbours = currentPoint.adjacentCardinal().filter { grid.getDataPoint(it) == FarmPlot.EMPTY }
            if (currentLevel == toLevel || neighbours.isEmpty())
                //++count
                border.add(currentPoint)
            neighbours.forEach { neighbour ->
                if (!visited.contains(Pair(neighbour, currentLevel+1))) {
                    visited.add(Pair(neighbour, currentLevel+1))
                    queue.add(Pair(neighbour, currentLevel+1))
                }
            }
        }
        return border
    }

    fun solvePart2(): Long {
        val gridSize = grid.getDimensions().first
        val distances: MutableMap<Point, Int> = mutableMapOf(start to 0)
        var reachablePoints: MutableList<Point> = mutableListOf(start)
        var level = 0
        val totals: MutableList<Long> = mutableListOf()
        val deltas: MutableList<Long> = mutableListOf()
        val deltaDeltas: MutableList<Long> = mutableListOf()
        var totalPoints = 0L
        while (level++ < 1000) {  // iterate up to max 1000 times just to be safe
            val pointsList: MutableList<Point> = mutableListOf()
            for (point in reachablePoints)
                for (adj in point.adjacentCardinal())
                    if (distances[adj] == null)
                        if (grid.getDataPoint(
                                // x,y extend infinitely so need to bring them back into the orig. grid
                                Point((adj.x % gridSize + gridSize) % gridSize, (adj.y % gridSize + gridSize) % gridSize)
                        ) != FarmPlot.ROCK) {
                            pointsList.add(adj)
                            distances[adj] = level
                        }
            if (level % 2 == 1) {
                totalPoints += pointsList.size
                // every 2 * gridSize + gridSize / 2 reaching the boundary of the grid
                if (level % (2 * gridSize) == gridSize / 2) {
                    totals.add(totalPoints)
                    val totalsSize = totals.size
                    if (totalsSize > 1)
                        deltas.add(totals[totalsSize - 1] - totals[totalsSize - 2])
                    val deltasSize = deltas.size
                    if (deltasSize > 1)
                        deltaDeltas.add(deltas[deltasSize - 1] - deltas[deltasSize - 2])
                    if (deltaDeltas.size > 1)
                        break
                }
            }
            reachablePoints = pointsList.toMutableList()
        }

        val targetLoopCount = (26501365 / (2 * gridSize) - 1).toLong()
        val currentLoopCount = (level / (2 * gridSize) - 1).toLong()
        val deltaLoopCountTriangular =
            targetLoopCount * (targetLoopCount + 1) / 2 - currentLoopCount * (currentLoopCount + 1) / 2
        val deltaDelta = deltaDeltas[deltaDeltas.size - 1]
        val initialDelta = deltas[0]
        return deltaDelta * deltaLoopCountTriangular + initialDelta * (targetLoopCount - currentLoopCount) + totalPoints
    }
}

enum class FarmPlot(val value: Char) {
    ROCK('#'),
    EMPTY('.'),
    BORDER('O'),
    START('S');
    companion object {
        val mapper: Map<Char,FarmPlot> = values().associateBy { it.value }
    }
}