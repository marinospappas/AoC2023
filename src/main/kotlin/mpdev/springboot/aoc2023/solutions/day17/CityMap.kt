package mpdev.springboot.aoc2023.solutions.day17

import mpdev.springboot.aoc2023.utils.*
import mpdev.springboot.aoc2023.utils.GridUtils.Direction.*

class CityMap(input: List<String>) {

    val grid = Grid(input, ('0'..'9').associateWith { it.digitToInt() }, border=0)
    val graph = SGraph(getConnected = ::getNeighbourPoints)
    private val start = Point(0,0)
    private val end = Point(grid.getMinMaxXY().x2, grid.getMinMaxXY().x4)
    // map: key Point and Direction, value GraphSate(Point/Direction/1) and Cost
    private val nextNeighbours: Map<Pair<Point,GridUtils.Direction>, Set<Pair<GraphState,Int>>>
    var maxStraightSteps = 3
    var minStraightSteps = 0

    init {
        val nextPtsMap = mutableMapOf<Pair<Point,GridUtils.Direction>, Set<Pair<GraphState,Int>>>()
        for (point in grid.getDataPoints().keys)
            for (direction in GridUtils.Direction.values()) {
                nextPtsMap[Pair(point, direction)] =
                    (GridUtils.Direction.values().toSet() - GridUtils.Direction.oppositeOf(direction))
                        .map { it.increment }
                        .map { Pair(GraphState(point + it, GridUtils.Direction.of(it), 1), grid.getDataPoint(point+it) ?: 1) }
                        .filter { grid.isInsideGrid(it.first.point) }
                        .toSet()
            }
        nextNeighbours = nextPtsMap.toMap()
    }

    fun findMinPath(): MinCostPath<GraphState> = graph.dijkstra(GraphState(start)) { state -> state.point == end }

    private fun getNeighbourPoints(location: GraphState): Set<Pair<GraphState, Int>> {
        val neighbours = mutableSetOf<Pair<GraphState, Int>>()
        if (location.point == start) {  // at the start, try both ways right and down
            for (dir in setOf(RIGHT, DOWN))
                neighbours.add(
                    Pair(
                        GraphState(start + dir.increment, dir, 1),
                        grid.getDataPoint(start + dir.increment) ?: 1
                    )
                )
        } else if (location.straightCount < minStraightSteps) {   // if we don't have the minimum straight steps, keep going straight
            val nextPoint = location.point + location.direction.increment
            neighbours.add(
                Pair(
                    GraphState(nextPoint, location.direction, location.straightCount + 1),
                    grid.getDataPoint(nextPoint) ?: 1
                )
            )
        } else {
            neighbours.addAll((GridUtils.Direction.values()
                .toSet() - GridUtils.Direction.oppositeOf(location.direction))
                .map { it.increment }
                .map {
                    Pair(GraphState(location.point + it, GridUtils.Direction.of(it), 1),
                        grid.getDataPoint(location.point + it) ?: 1)
                }
                .filter { grid.isInsideGrid(it.first.point) }
                .toSet())
            //neighbours.addAll(nextNeighbours[Pair(location.point, location.direction)]?.toMutableSet() ?: throw AocException("ERROR in neighbours for $location \n $nextNeighbours"))
            neighbours.forEach {
                it.first.straightCount = if (it.first.direction == location.direction)
                    location.straightCount + 1
                else 1
            }
        }
        // we can oly reach the end only if we have done the minimum number of straight steps
        neighbours.removeIf { it.first.point == end && it.first.straightCount < minStraightSteps }
        neighbours.removeIf { !grid.isInsideGrid(it.first.point) }      // make sure we stay inside the grid
        // we cannot exceed the maximum number of straight steps
        return neighbours.filter { it.first.straightCount <= maxStraightSteps }.toSet()
    }

    data class GraphState(val point: Point, val direction: GridUtils.Direction = RIGHT, var straightCount: Int = 0)
}