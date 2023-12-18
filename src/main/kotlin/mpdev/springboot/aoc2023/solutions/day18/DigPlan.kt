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

    fun digArea(digInstructions: List<DigInstr>): Long {
        var area = 10L  // instead of using BigDecimal, use Long * 10 as we only have 1 dec.digit
        var posX = 0L
        digInstructions.forEach { dig ->
            val dx = dig.length * dig.direction.increment.x
            val dy = dig.length * dig.direction.increment.y
            posX += dx
            area += dy * posX * 10 + dig.length * 10 / 2
        }
        return area / 10
    }
}

data class DigInstr(val direction: GridUtils.Direction, val length: Int, val colour: Int)

enum class Plot(val value: Char) {
    DUG_UP('#');
    companion object {
        val mapper: Map<Char,Plot> = values().associateBy { it.value }
    }
}