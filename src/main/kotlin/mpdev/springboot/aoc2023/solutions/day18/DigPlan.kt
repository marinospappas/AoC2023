package mpdev.springboot.aoc2023.solutions.day18

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.*
import mpdev.springboot.aoc2023.utils.GridUtils.Direction.*

@Serializable
@AocInClass(delimiters = [" "], removePatterns = ["\\(#", "\\)"])
data class AoCInput(
    // R 6 (#70c710)
    // 0 1   2
    @AocInField(0) val direction: String,
    @AocInField(1) val length: Int,
    @AocInField(2) val colour: String
)

class DigPlan(input: List<String>) {

    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    val digDirections: List<DigInstr> = aocInputList.map {
        DigInstr(GridUtils.Direction.of(it.direction), it.length, Integer.parseInt(it.colour, 16))
    }
    lateinit var grid: Grid<Plot>
    val digDirections2: List<DigInstr> = aocInputList.map {
        DigInstr(
            when(it.colour.last()){'0' -> RIGHT; '1' -> DOWN; '2' -> LEFT; else -> UP},
            Integer.parseInt(it.colour.removeRange(it.colour.lastIndex, it.colour.lastIndex+1), 16),
            0)
    }

    // grid not needed but left here for visualization
    fun digTrench(): List<Point> {
        val points = mutableListOf(Point(0,0))
        digDirections.forEach { dig ->
            repeat(dig.length) {
                points.add(points.last() + dig.direction.increment)
            }
        }
        grid = Grid(points.toTypedArray(), Plot.mapper, border = 0)
        return points
    }

    /**
     * Pick's theorem
     * P: number of points on the perimeter of the lattice polygon
     * I: number of points inside the polygon
     * A: area of the polygon
     * A = I + P/2 - 1  (or  I = A - P/2 + 1)
     * We need to calculate I + P as the digger digs a 1 cubic metre hole at every lattice point
     * or (A - P/2 + 1) + (P) or A + P/2 + 1
     */
    fun digVolume(digInstructions: List<DigInstr>): Long {
        val polygonArea = digInstructions.fold(Pair(0L,0L)) { acc, dig -> // the pair holds the current sum and the current x
            val dx = dig.length * dig.direction.increment.x.toLong()
            val dy = dig.length * dig.direction.increment.y.toLong()
            val x = acc.second + dx
            Pair(acc.first + x * dy, x)
        }.first
        val pointsOnPerimeter = digInstructions.sumOf { it.length }
        return polygonArea + pointsOnPerimeter / 2 + 1
    }
}

data class DigInstr(val direction: GridUtils.Direction, val length: Int, val colour: Int = 0)

enum class Plot(val value: Char) {
    DUG_UP('#');
    companion object {
        val mapper: Map<Char,Plot> = values().associateBy { it.value }
    }
}