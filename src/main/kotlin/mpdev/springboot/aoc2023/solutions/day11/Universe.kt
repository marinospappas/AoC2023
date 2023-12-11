package mpdev.springboot.aoc2023.solutions.day11

import mpdev.springboot.aoc2023.utils.GridL
import mpdev.springboot.aoc2023.utils.PointL

class Universe(input: List<String>) {

    val grid = GridL(input, Space.mapper, border = 0)

    fun expand(factor: Int) {
        val (minx, maxx, miny, maxy) = grid.getMinMaxXY()
        // expand x
        val emptyCols = mutableListOf<Long>()
        for (x in (minx..maxx))
            if (grid.getDataPoints().count { it.key.x == x } == 0)
                emptyCols.add(x)
        expandOneDimension(Dimension.X, emptyCols, factor)
        // expand y
        val emptyRows = mutableListOf<Long>()
        for (y in (miny..maxy))
            if (grid.getDataPoints().count { it.key.y == y } == 0)
                emptyRows.add(y)
        expandOneDimension(Dimension.Y, emptyRows, factor)
    }

    private fun expandOneDimension(dimension: Dimension, listOfEmpty: List<Long>, factor: Int ) {
        val checkList = listOfEmpty + Long.MAX_VALUE
        val condition: (PointL, Int) -> Boolean =
            if (dimension == Dimension.X)
                { p,indx -> p.x in checkList[indx] .. checkList[indx+1] }
            else
                { p,indx -> p.y in checkList[indx] .. checkList[indx+1] }
        for (i in checkList.lastIndex-1 downTo 0) {
            grid.getDataPoints().filter { condition(it.key, i) }
                .forEach { (p, v) ->
                    grid.removeDataPoint(p)
                    if (dimension == Dimension.X)
                        grid.setDataPoint(PointL(p.x + (i+1)*(factor-1), p.y), v)
                    else
                        grid.setDataPoint(PointL(p.x, p.y + (i+1)*(factor-1)), v)
                }
        }
        grid.updateDimensions()
    }

    fun measureDistances(): List<Long> {
        val galaxies = grid.getDataPoints().filter { (_,v) -> v == Space.GALAXY }
            .map { it.key }
        val distances = mutableListOf<Long>()
        for (i in 0 until galaxies.lastIndex)
            for (j in i+1 ..galaxies.lastIndex)
                distances.add(galaxies[i].manhattan(galaxies[j]))
        return distances
    }
}

enum class Dimension{ X, Y }
enum class Space(val value: Char) {
    GALAXY('#');
    companion object {
        val mapper: Map<Char,Space> = values().associateBy { it.value }
    }
}