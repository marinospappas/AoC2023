package mpdev.springboot.aoc2023.utils

import kotlin.math.abs

class HexGrid<T>(input: List<String> = emptyList(), mapper: Map<Char,T>): Grid<T>(input, mapper) {

    fun setDimensions(maxX: Int, maxY: Int, minX: Int = 0, minY: Int = 0) {
        this.maxX = maxX
        this.maxY = maxY
        this.minX = minX
        this.minY = minY
    }

    override fun updateDimensions() {
        super.updateDimensions()
        if (maxX % 2 != 0) ++maxX
        if (maxY % 2 != 0) ++maxY
        if (minX % 2 != 0) --minX
        if (minY % 2 != 0) --minY
    }

    override fun getDataPoint(p: Point): T? {
        if (!isValidHexGridPoint(p))
            throw AocException("invalid hex grid point (x=${p.x}, y=${p.y})")
        return super.getDataPoint(p)
    }
    override fun setDataPoint(p: Point, t: T) {
        if (!isValidHexGridPoint(p))
            throw AocException("invalid hex grid point (x=${p.x}, y=${p.y})")
        super.setDataPoint(p, t)
    }

    fun extendGrid() {
        minX -= 2
        maxX += 2
        minY -= 2
        maxY += 2
    }

    fun getAllPointsInGrid(): Set<Point> {
        val points = mutableSetOf<Point>()
        (minX..maxX).forEach { x -> (minY..maxY).forEach { y ->
            if (isValidHexGridPoint(Point(x,y))) points.add(Point(x,y)) } }
        return points
    }

    fun getAdjacent(p: Point) = Directions.values().map { p + it.step }.toSet()

    private fun isValidHexGridPoint(p: Point) = (p.y % 2 == 0 && p.x % 2 == 0) || (abs(p.y % 2) == 1 && abs(p.x % 2) == 1)

    override fun print() {
        printGrid(data2Grid())
    }

    private fun data2Grid(): Array<CharArray> {
        val grid: Array<CharArray> = Array(maxY-minY+1) { y -> CharArray(maxX-minX+1) { x ->
            if (isValidHexGridPoint(Point(x,y))) DEFAULT_CHAR else ' '
        } }
        data.forEach { (pos, item) -> grid[pos.y - minY][pos.x - minX] = map2Char(item) }
        return grid
    }

    enum class Directions(val strValue: String, val step: Point) {
        E("e", Point(2, 0)),
        SE("se", Point(1, -1)),
        SW("sw", Point(-1, -1)),
        W("w", Point(-2, 0)),
        NW("nw", Point(-1, 1)),
        NE("ne", Point(1, 1));

        companion object {
            fun from(s: String) = values().first { it.strValue == s }
        }
    }
}