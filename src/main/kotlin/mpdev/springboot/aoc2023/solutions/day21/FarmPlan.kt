package mpdev.springboot.aoc2023.solutions.day21

import mpdev.springboot.aoc2023.utils.*
import mpdev.springboot.aoc2023.solutions.day21.FarmPlot.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class FarmPlan(input: List<String>) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    val grid = Grid(input, FarmPlot.mapper, border=0)
    val start = grid.getDataPoints().filter { it.value == START }.keys.firstOrNull()
        ?: throw AocException("no starting point")
    private val patternSize = grid.getDimensions().first  // assumed square
    private val superGridSteps = 26_501_365L // 202300 * 131 + 65
    val FACTOR = (superGridSteps - patternSize/2) / patternSize   // 202300
    val simulationFactor = 4

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
     * The tile indexes used below refer to the indexes in super_gir_simulation.txt
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
        val simPoints = traverseGrid(simStart, simulationFactor * patternSize + patternSize / 2, simGrid)
        log.info("simulation factor {}, points reached {}", simulationFactor, simPoints.count())

        val pointsOdd = getCountOfTile(13, simulationFactor, simPoints)
        val pointsEven = getCountOfTile(22, simulationFactor, simPoints)
        val pointsTop = getCountOfTile(4, simulationFactor, simPoints)
        val pointsRight = getCountOfTile(44, simulationFactor, simPoints)
        val pointsBottom = getCountOfTile(76, simulationFactor, simPoints)
        val pointsLeft = getCountOfTile(36, simulationFactor, simPoints)
        val pointsA1 = getCountOfTile(12, simulationFactor, simPoints)
        val pointsB1 = getCountOfTile(3, simulationFactor, simPoints)
        val pointsA2 = getCountOfTile(14, simulationFactor, simPoints)
        val pointsB2 = getCountOfTile(5, simulationFactor, simPoints)
        val pointsA3 = getCountOfTile(52, simulationFactor, simPoints)
        val pointsB3 = getCountOfTile(53, simulationFactor, simPoints)
        val pointsA4 = getCountOfTile(46, simulationFactor, simPoints)
        val pointsB4 = getCountOfTile(45, simulationFactor, simPoints)

        val result = countOdd * pointsOdd + countEven * pointsEven +
                pointsTop + pointsRight + pointsBottom + pointsLeft +
                countA * (pointsA1 + pointsA2 + pointsA3 + pointsA4) +
                countB * (pointsB1 + pointsB2 + pointsB3 + pointsB4)

        return result
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