package mpdev.springboot.aoc2023.solutions.day17

import mpdev.springboot.aoc2023.utils.*
import mpdev.springboot.aoc2023.utils.GridUtils.Direction.*

class CityMap(input: List<String>) {

    val grid = Grid(input, ('0'..'9').associateWith { it.digitToInt() }, border=0)
    val graph = SGraph(getConnected = ::getNeighbourPoints)
    private val start = Point(0,0)
    private val end = Point(grid.getMinMaxXY().x2, grid.getMinMaxXY().x4)
    var maxStraightSteps = 3
    var minStraightSteps = 0

    fun findMinPath(): MinCostPath<GraphState> = graph.dijkstra(GraphState(start)) { state -> state.point == end }

    //TODO: make the below function less verbose
    private fun getNeighbourPoints(location: GraphState): Set<Pair<GraphState,Int>> {
        val neighbours = mutableSetOf<Pair<GraphState,Int>>()
        if (location.straightCount < minStraightSteps-1) {
            val nextPoint = location.point + location.direction.increment
            if (nextPoint != end || location.straightCount >= minStraightSteps-2)
            neighbours.add(Pair(GraphState(nextPoint, location.direction, location.straightCount + 1),
                    grid.getDataPoint(nextPoint) ?: 0))
        }
        else
            when (location.direction) {
                RIGHT -> {
                    neighbours.add(Pair(GraphState(location.point + RIGHT.increment, RIGHT, location.straightCount+1),
                        grid.getDataPoint(location.point + RIGHT.increment)?:0))
                    neighbours.add(Pair(GraphState(location.point + UP.increment, UP, 0),
                        grid.getDataPoint(location.point + UP.increment)?:0))
                    neighbours.add(Pair(GraphState(location.point + DOWN.increment, DOWN, 0),
                        grid.getDataPoint(location.point + DOWN.increment)?:0))
                }
                DOWN -> {
                    neighbours.add(Pair(GraphState(location.point + DOWN.increment, DOWN, location.straightCount+1),
                        grid.getDataPoint(location.point + DOWN.increment)?:0))
                    neighbours.add(Pair(GraphState(location.point + LEFT.increment, LEFT, 0),
                        grid.getDataPoint(location.point + LEFT.increment)?:0))
                    neighbours.add(Pair(GraphState(location.point + RIGHT.increment, RIGHT, 0),
                        grid.getDataPoint(location.point + RIGHT.increment)?:0))
                }
                LEFT -> {
                    neighbours.add(Pair(GraphState(location.point + LEFT.increment, LEFT, location.straightCount+1),
                        grid.getDataPoint(location.point + LEFT.increment)?:0))
                    neighbours.add(Pair(GraphState(location.point + UP.increment, UP, 0),
                        grid.getDataPoint(location.point + UP.increment)?:0))
                    neighbours.add(Pair(GraphState(location.point + DOWN.increment, DOWN, 0),
                        grid.getDataPoint(location.point + DOWN.increment)?:0))
                }
                UP -> {
                    neighbours.add(Pair(GraphState(location.point + UP.increment, UP, location.straightCount+1),
                        grid.getDataPoint(location.point + UP.increment)?:0))
                    neighbours.add(Pair(GraphState(location.point + LEFT.increment, LEFT, 0),
                        grid.getDataPoint(location.point + LEFT.increment)?:0))
                    neighbours.add(Pair(GraphState(location.point + RIGHT.increment, RIGHT, 0),
                        grid.getDataPoint(location.point + RIGHT.increment)?:0))
                }
            }
        val result = neighbours.filter { grid.isInsideGrid(it.first.point) }
            .filter { it.first.straightCount < maxStraightSteps }
            .toSet()
        return result
    }

    data class GraphState(var point: Point, var direction: GridUtils.Direction = RIGHT, var straightCount: Int = 0)
}