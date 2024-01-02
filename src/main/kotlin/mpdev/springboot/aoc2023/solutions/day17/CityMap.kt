package mpdev.springboot.aoc2023.solutions.day17

import mpdev.springboot.aoc2023.utils.*
import mpdev.springboot.aoc2023.utils.GridUtils.Direction.*

class CityMap(input: List<String>) {

    val grid = Grid(input, ('0'..'9').associateWith { it.digitToInt() }, border=0)
    val graph = SGraph(getConnected = ::getNeighbourPoints)
    private val start = Point(0,0)
    private val end = Point(grid.getMinMaxXY().x2, grid.getMinMaxXY().x4)
    // map: key Point and Direction, value GraphSate(Point/Direction/1) and Cost
    private val nextNeighbours: Map<GraphState, Set<Pair<GraphState,Int>>>
    var maxStraightSteps = 3
    var minStraightSteps = 0

    init {
        val nextPtsMap = mutableMapOf<GraphState, Set<Pair<GraphState,Int>>>()
        for (point in grid.getDataPoints().keys)
            for (direction in GridUtils.Direction.values()) {
                nextPtsMap[GraphState(point, direction)] =
                    (GridUtils.Direction.values().toSet() - GridUtils.Direction.oppositeOf(direction))
                        .map { it.increment }
                        .map { Pair(GraphState(point + it, GridUtils.Direction.of(it), 1), grid.getDataPoint(point+it) ?: 1) }
                        .filter { grid.isInsideGrid(it.first.point) }
                        .toSet()
            }
        nextNeighbours = nextPtsMap.toMap()
    }

    fun findMinPath(): MinCostPath<GraphState> = graph.dijkstra(GraphState(start)) { state -> state.point == end }

    // TODO: save the connected points to map to improve performance
    private fun getNeighbourPoints(location: GraphState): Set<Pair<GraphState, Int>> {
        val neighbours = mutableSetOf<Pair<GraphState, Int>>()
        if (location.point == start) {  // at the start, try both ways right and down
            for (dir in setOf(RIGHT, DOWN))
                neighbours.add(
                    Pair(GraphState(start + dir.increment, dir, 1),
                        grid.getDataPoint(start + dir.increment) ?: 1)
                )
        } else if (location.straightCount < minStraightSteps) {   // if we don't have the minimum straight steps, keep going straight
            neighbours.add(
                Pair(GraphState( location.point + location.direction.increment, location.direction, location.straightCount + 1),
                    grid.getDataPoint( location.point + location.direction.increment) ?: 1)
            )
        } else {
            neighbours.addAll((GridUtils.Direction.values().toSet() - GridUtils.Direction.oppositeOf(location.direction))
                .map { it.increment }
                .map {
                    Pair(GraphState(location.point + it, GridUtils.Direction.of(it), 1),
                        grid.getDataPoint(location.point + it) ?: 1)
                }
                .filter { grid.isInsideGrid(it.first.point) }
                .toSet())
            //neighbours.addAll(nextNeighbours[GraphState(location.point, location.direction)]?.toMutableSet() ?: throw AocException("ERROR in neighbours for $location \n $nextNeighbours"))
            neighbours.forEach {
                it.first.straightCount = if (it.first.direction == location.direction)
                    location.straightCount + 1
                else 1
            }
        }
        // we can only reach the end if we have done the minimum number of straight steps
        neighbours.removeIf { it.first.point == end && it.first.straightCount < minStraightSteps }
        // we cannot exceed the maximum number of straight steps
        return neighbours.filter { it.first.straightCount <= maxStraightSteps }.toSet()
    }

    data class GraphState(val point: Point, val direction: GridUtils.Direction = RIGHT, var straightCount: Int = 0)
}