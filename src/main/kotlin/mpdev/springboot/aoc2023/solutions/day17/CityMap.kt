package mpdev.springboot.aoc2023.solutions.day17

import mpdev.springboot.aoc2023.utils.*
import mpdev.springboot.aoc2023.utils.GridUtils.Direction.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.system.measureNanoTime

class CityMap(input: List<String>) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    val grid = Grid(input, ('0'..'9').associateWith { it.digitToInt() }, border = 0)
    val graph = SGraph(getConnected = ::getNeighbourPoints, heuristic = ::heuristic)
    private val start = Point(0, 0)
    private val maxX = grid.getMinMaxXY().x2
    private val maxY = grid.getMinMaxXY().x4
    private val end = Point(maxX, maxY)

    var maxStraightSteps = 3
    var minStraightSteps = 0

    private fun heuristic(state: GraphState): Int {
        return state.point.manhattan(end)
    }

    fun findMinPath(): MinCostPath<GraphState> {
        totalTimeSpentInGetConnected = 0L
        //return (graph.dijkstra(GraphState(start)) { state -> state.point == end })
        return (graph.aStar(GraphState(start)) { state -> state.point == end })
            .also {
                log.info("number of iterations: {}", it.numberOfIterations)
                log.info("time in getConnected: {} milli-sec", totalTimeSpentInGetConnected / 1000000)
            }
    }

    private var totalTimeSpentInGetConnected = 0L

    private fun getNeighbourPoints(location: GraphState): Set<Pair<GraphState, Int>> {
        val neighbours = mutableSetOf<Pair<GraphState, Int>>()
        val elapsed = measureNanoTime {
            if (location.point == start) {  // at the start, try both ways right and down
                for (dir in setOf(RIGHT, DOWN))
                    neighbours.add(Pair(GraphState(start + dir.increment, dir, 1), grid.getDataPoint(start + dir.increment) ?: 1))
            } else if (location.straightCount < minStraightSteps) {   // if we don't have the minimum straight steps, keep going straight
                neighbours.add(
                    Pair(GraphState(location.point + location.direction.increment, location.direction, location.straightCount + 1),
                        grid.getDataPoint(location.point + location.direction.increment) ?: 1)
                )
            } else {
                for (dir in GridUtils.Direction.values()) {
                    if (dir == Companion.oppositeOf(location.direction))   // we cannot backtrack
                        continue
                    val newPoint = location.point + dir.increment
                    if (newPoint.x < 0 || newPoint.x > maxX || newPoint.y < 0 || newPoint.y > maxY)
                        continue
                    neighbours.add(Pair(GraphState(newPoint, dir, if (dir == location.direction) location.straightCount+1 else 1), grid.getDataPoint(newPoint) ?: 1))
                }
            }
            // we can only reach the end if we have done the minimum number of straight steps
            neighbours.removeIf { it.first.point == end && it.first.straightCount < minStraightSteps }
        }
        totalTimeSpentInGetConnected += elapsed
        // we cannot exceed the maximum number of straight steps
        return neighbours.filter { it.first.straightCount <= maxStraightSteps }.toSet()
    }

    data class GraphState(val point: Point, val direction: GridUtils.Direction = RIGHT, var straightCount: Int = 0)
}