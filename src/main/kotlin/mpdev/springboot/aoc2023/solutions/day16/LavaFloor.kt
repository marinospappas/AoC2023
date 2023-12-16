package mpdev.springboot.aoc2023.solutions.day16

import mpdev.springboot.aoc2023.solutions.day16.Tile.*
import mpdev.springboot.aoc2023.utils.Grid
import mpdev.springboot.aoc2023.utils.GridUtils
import mpdev.springboot.aoc2023.utils.GridUtils.Direction.*
import mpdev.springboot.aoc2023.utils.Point

class LavaFloor(input: List<String>) {

    var debug = false
    val grid = Grid(input, Tile.mapper, border = 0)

    fun simulateBeam(beamStart: Beam): Int {
        val energisedGrid = Grid(grid.getDataPoints(), Tile.mapper, border = 0)
        val visited = mutableSetOf<Beam>()
        val queue = ArrayDeque<Beam>().also { q -> q.add(beamStart) }
        while (queue.isNotEmpty()) {
            val beam = queue.removeFirst().also { visited.add(it) }
            energisedGrid.setDataPoint(beam.p, ENERGISED)
            getNextBeams(beam).forEach { nextBeam ->
                if (!visited.contains(nextBeam) && grid.isInsideGrid(nextBeam.p))
                    queue.add(nextBeam)
            }
        }
        if (debug)
            energisedGrid.print()
        return energisedGrid.countOf(ENERGISED)
    }

    fun getPerimeterBeams(): Set<Beam> {
        val (minx, maxx, miny, maxy) = grid.getMinMaxXY()
        val perimeter = mutableSetOf<Beam>()
        for (x in minx..maxx) {
            perimeter.add(Beam(Point(x,miny), DOWN))
            perimeter.add(Beam(Point(x,maxy), UP))
        }
        for (y in miny .. maxy) {
            perimeter.add(Beam(Point(minx,y), RIGHT))
            perimeter.add(Beam(Point(maxx,y), LEFT))
        }
        return perimeter
    }

    // simulate beam movement via the mirrors
    private fun getNextBeams(beam: Beam): List<Beam> {
        val curPos = beam.p
        val curDir = beam.dir
        val result = mutableListOf<Beam>()
        when {
            curDir == RIGHT && grid.getDataPoint(curPos) == null -> result.add(Beam(curPos + Point(1,0), RIGHT))
            curDir == RIGHT && grid.getDataPoint(curPos) == MIRROR_R -> result.add(Beam(curPos + Point(0,-1), UP))
            curDir == RIGHT && grid.getDataPoint(curPos) == MIRROR_L -> result.add(Beam(curPos + Point(0,1), DOWN))
            curDir == RIGHT && grid.getDataPoint(curPos) == SPLIT_H -> result.add(Beam(curPos + Point(1,0), RIGHT))
            curDir == RIGHT && grid.getDataPoint(curPos) == SPLIT_V -> {
                result.add(Beam(curPos + Point(0,-1) , UP))
                result.add(Beam(curPos + Point(0,1) , DOWN))
            }

            curDir == LEFT && grid.getDataPoint(curPos) == null -> result.add(Beam(curPos + Point(-1,0), LEFT))
            curDir == LEFT && grid.getDataPoint(curPos) == MIRROR_L -> result.add(Beam(curPos + Point(0,-1), UP))
            curDir == LEFT && grid.getDataPoint(curPos) == MIRROR_R -> result.add(Beam(curPos + Point(0,1), DOWN))
            curDir == LEFT && grid.getDataPoint(curPos) == SPLIT_H -> result.add(Beam(curPos + Point(-1,0), LEFT))
            curDir == LEFT && grid.getDataPoint(curPos) == SPLIT_V -> {
                result.add(Beam(curPos + Point(0,-1) , UP))
                result.add(Beam(curPos + Point(0,1) , DOWN))
            }

            curDir == UP && grid.getDataPoint(curPos) == null -> result.add(Beam(curPos + Point(0,-1), UP))
            curDir == UP && grid.getDataPoint(curPos) == MIRROR_R -> result.add(Beam(curPos + Point(1,0), RIGHT))
            curDir == UP && grid.getDataPoint(curPos) == MIRROR_L -> result.add(Beam(curPos + Point(-1,0), LEFT))
            curDir == UP && grid.getDataPoint(curPos) == SPLIT_V -> result.add(Beam(curPos + Point(0,-1), UP))
            curDir == UP && grid.getDataPoint(curPos) == SPLIT_H -> {
                result.add(Beam(curPos + Point(-1,0) , LEFT))
                result.add(Beam(curPos + Point(1,0) , RIGHT))
            }

            curDir == DOWN && grid.getDataPoint(curPos) == null -> result.add(Beam(curPos + Point(0,1), DOWN))
            curDir == DOWN && grid.getDataPoint(curPos) == MIRROR_L -> result.add(Beam(curPos + Point(1,0), RIGHT))
            curDir == DOWN && grid.getDataPoint(curPos) == MIRROR_R -> result.add(Beam(curPos + Point(-1,0), LEFT))
            curDir == DOWN && grid.getDataPoint(curPos) == SPLIT_V -> result.add(Beam(curPos + Point(0,1), DOWN))
            curDir == DOWN && grid.getDataPoint(curPos) == SPLIT_H -> {
                result.add(Beam(curPos + Point(-1,0) , LEFT))
                result.add(Beam(curPos + Point(1,0) , RIGHT))
            }
        }
        return result
    }
}

data class Beam(val p: Point, val dir: GridUtils.Direction)

enum class Tile(val value: Char) {
    MIRROR_R('/'),
    MIRROR_L('\\'),
    SPLIT_H('-'),
    SPLIT_V('|'),
    ENERGISED('#');
    companion object {
        val mapper: Map<Char,Tile> = values().associateBy { it.value }
    }
}