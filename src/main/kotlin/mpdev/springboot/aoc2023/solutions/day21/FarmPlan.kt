package mpdev.springboot.aoc2023.solutions.day21

import mpdev.springboot.aoc2023.utils.*
import mpdev.springboot.aoc2023.solutions.day21.FarmPlot.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class FarmPlan(input: List<String>) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)
    var debug = false

    val grid = Grid(input, FarmPlot.mapper, border=0)
    val start = grid.getDataPoints().filter { it.value == START }.keys.firstOrNull()
        ?: throw AocException("no starting point")
    private val patternSize = grid.getDimensions().first  // assumed square
    private val superGridSteps = 26_501_365L // 202300 * 131 + 65
    val FACTOR = (superGridSteps - patternSize/2) / patternSize   // 202300
    val simulationFactor = 2    // 5 x 5 super grid used for simulation (2 x 2 + 1)

    fun traverseGrid(start: Point, toLevel: Int, workGrid: Grid<FarmPlot> = grid): Set<Point> {
        val pointsReached = mutableSetOf<Point>()
        val queue = ArrayDeque<Pair<Point, Int>>().also { it.add(start to toLevel) }
        val visited = mutableSetOf<Point>()
        while (queue.isNotEmpty()) {
            val (currentPoint, levelsToGo) = queue.removeFirst()
            // all even level points are part of the answer as the elf may just go one step forward, one step back
            if (levelsToGo % 2 == 0)
                pointsReached.add(currentPoint)
            if (levelsToGo > 0) {
                val neighbours = currentPoint.adjacentCardinal().filter { workGrid.getDataPoint(it) == EMPTY }
                neighbours.filterNot { visited.contains(it) }.forEach { p ->
                    queue.add(p to levelsToGo - 1)
                    visited.add(p)
                }
            }
        }
        return pointsReached
    }

    /**
     * part 2 is solved by simulating the final grid using a 9 x 9 grid of the original pattern
     * (file super_grid_simulation.txt)
     * given that (a) the total steps are an even multiple of the pattern size + half the pattern size
     * (b) the points reached expand in a diamond shape across the grid and
     * (c) the corners of the diamond will reach the edge of the tile first after pattern size / 2
     *     and then every pattern size steps
     * the 9 x 9 super gird simulates the final grid successfully
     * and gives us the formulas to calculate the final number of each kind of tile
     * For the solution though, a 5 x 5 super grid (factor 2) has been used for performance
     * The tile indexes used below refer to the indexes in super_gir_simulation.txt in () - these are the indexes for the 5x5 grid
     */
    fun solvePart2(factor: Long): Long {
        // numbers of different patterns
        val countOdd = factor * (factor + 1) / 2 + (factor - 1) * factor / 2
        val countEven = (factor - 1) * factor / 2 + (factor - 2) * (factor - 1) / 2
        val countA = factor - 1
        val countB = factor

        // counts of reached points in each pattern - simulate using a super grid of 9x9 pattern
        val simGrid = createSimualtionGrid(simulationFactor)
        val simStart = simGrid.getDataPoints().filter { it.value == START }.keys.first()
        // the numbers of steps to run for the simulation is the coordinate of the start as this is in the middle of the super grid
        // and we want to simulate from the middle to the edge
        val simPoints = traverseGrid(simStart, simStart.x, simGrid)
        log.info("simulation factor {}, points reached {}", simulationFactor, simPoints.count())

        if (debug) {
            simPoints.forEach { p -> simGrid.setDataPoint(p, REACHED) }
            simGrid.print()
        }
        val tilesIndex = mapOf( // the indexes of each kind of tile for different simulation steps
            4 to intArrayOf(13, 22, 4, 44, 76, 36, 12, 3, 14, 5, 52, 53, 46, 45),    // for 9 x 9 super grid
            2 to intArrayOf(7, 12, 2, 14, 22, 10, 6, 1, 8, 3, 18, 23, 16, 21)    // for 5 x 5 super grid
        )
        val pointsOdd = getCountOfTile(tilesIndex[simulationFactor]?.get(0)!!, simulationFactor, simPoints)
        val pointsEven = getCountOfTile(tilesIndex[simulationFactor]?.get(1)!!, simulationFactor, simPoints)
        val pointsTop = getCountOfTile(tilesIndex[simulationFactor]?.get(2)!!, simulationFactor, simPoints)
        val pointsRight = getCountOfTile(tilesIndex[simulationFactor]?.get(3)!!, simulationFactor, simPoints)
        val pointsBottom = getCountOfTile(tilesIndex[simulationFactor]?.get(4)!!, simulationFactor, simPoints)
        val pointsLeft = getCountOfTile(tilesIndex[simulationFactor]?.get(5)!!, simulationFactor, simPoints)
        val pointsA1 = getCountOfTile(tilesIndex[simulationFactor]?.get(6)!!, simulationFactor, simPoints)
        val pointsB1 = getCountOfTile(tilesIndex[simulationFactor]?.get(7)!!, simulationFactor, simPoints)
        val pointsA2 = getCountOfTile(tilesIndex[simulationFactor]?.get(8)!!, simulationFactor, simPoints)
        val pointsB2 = getCountOfTile(tilesIndex[simulationFactor]?.get(9)!!, simulationFactor, simPoints)
        val pointsA3 = getCountOfTile(tilesIndex[simulationFactor]?.get(10)!!, simulationFactor, simPoints)
        val pointsB3 = getCountOfTile(tilesIndex[simulationFactor]?.get(11)!!, simulationFactor, simPoints)
        val pointsA4 = getCountOfTile(tilesIndex[simulationFactor]?.get(12)!!, simulationFactor, simPoints)
        val pointsB4 = getCountOfTile(tilesIndex[simulationFactor]?.get(13)!!, simulationFactor, simPoints)

        return  countOdd * pointsOdd + countEven * pointsEven +
                pointsTop + pointsRight + pointsBottom + pointsLeft +
                countA * (pointsA1 + pointsA2 + pointsA3 + pointsA4) +
                countB * (pointsB1 + pointsB2 + pointsB3 + pointsB4)
    }

    fun getCountOfTile(tileIndx: Int, factor: Int, pointsSet: Set<Point>): Int {
        val row = tileIndx / (2*factor + 1)
        val col = tileIndx % (2*factor + 1)
        return pointsSet.count { p ->
            p.x in patternSize*col until patternSize*(col+1) && p.y in patternSize*row until patternSize*(row+1)
        }
    }

    fun createSimualtionGrid(factor: Int): Grid<FarmPlot> {
        val simMap = mutableMapOf<Point,FarmPlot>()
        // multiply to the right
        grid.getDataPoints().forEach { (p,v) ->
            for (i in 0 until 2*factor + 1)
                simMap[Point(p.x + i * patternSize, p.y)] = if (v == START) EMPTY else v
        }
        // multiply down
        simMap.toMap().forEach { (p,v) ->
            for (i in 0 until 2*factor + 1)
                simMap[Point(p.x, p.y + i * patternSize)] = v
        }
        val start = Point(
            (2*factor + 1)/2 * patternSize + patternSize/2, (2*factor + 1)/2 * patternSize + patternSize/2
        )
        simMap[start] = START
        return Grid(simMap, FarmPlot.mapper, border=0)
    }
}

enum class FarmPlot(val value: Char) {
    ROCK('#'),
    EMPTY('.'),
    REACHED('O'),
    START('S');
    companion object {
        val mapper: Map<Char,FarmPlot> = values().associateBy { it.value }
    }
}