package mpdev.springboot.aoc2023.solutions.day24

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.*
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

@Serializable
@AocInClass(delimiters = ["@"])
data class AoCInput(
    // 18, 19, 22 @ -1, -1, -2
    // 0            1
    @AocInField(0, delimiters = [","]) val position: List<Long>,
    @AocInField(1, delimiters = [","]) val velocity: List<Long>
)

class HailStones(input: List<String>) {

    var debug = false
    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    val stones: List<Stone> = aocInputList.map { Stone(Point3DL(it.position), Point3DL(it.velocity)) }
    var testArea = LongRange(200000000000000, 400000000000000)
    // estimated V range for the throw
    val vMin = stones.minOf { it.velocity.toList().min() } * 2
    val vMax = stones.maxOf { it.velocity.toList().max() } * 2
    val `throw` = Stone()

    fun calculateIntersections(): Int {
        val intersectTimesXY = mutableMapOf<Pair<Int,Int>, Pair<Double,Double>>()
        // for each combi of two stones get t1 and t2 resp. when the 2 trajectories pass from the same point
        for (i in 0 .. stones.lastIndex-1)
            for (j in i+1 .. stones.lastIndex) {
                intersectTimesXY[Pair(i,j)] = stones[i].timesForXYIntersect(stones[j])
            }
        // for each intersection in the future calculate x and y
        val positionsXYAtIntersectTimes = intersectTimesXY.mapNotNull { e ->
            val stone1Id = e.key.first
            val time1 = e.value.first
            val time2 = e.value.second
            if (!time1.isNaN() && time1 > 0 && time2 > 0)
                e.key to stones[stone1Id].posXYForTime(time1)
            else
                null
        }.toMap().also { if (debug) it.forEach {e -> e.println()} }
        // count intersections in the test area
        return positionsXYAtIntersectTimes.values.count { (x, y) ->
            x > testArea.first.toDouble() && x < testArea.last.toDouble() &&
                    y > testArea.first.toDouble() && y < testArea.last.toDouble()
        }
    }

    fun calculateThrow(): Stone {
        // step 1 calculate speeds
        val vx = mutableListOf<Set<Long>>()
        val vy = mutableListOf<Set<Long>>()
        val vz = mutableListOf<Set<Long>>()
        for (i in 0 .. stones.lastIndex-1)
            for (j in i+1 .. stones.lastIndex) {
                if (stones[i].velocity.x == stones[j].velocity.x)
                    vx.add(calculatePossibbleVThrow(Pair(stones[i].position.x, stones[j].position.x), stones[i].velocity.x))
                if (stones[i].velocity.y == stones[j].velocity.y)
                    vy.add(calculatePossibbleVThrow(Pair(stones[i].position.y, stones[j].position.y), stones[i].velocity.y))
                if (stones[i].velocity.z == stones[j].velocity.z)
                    vz.add(calculatePossibbleVThrow(Pair(stones[i].position.z, stones[j].position.z), stones[i].velocity.z))
            }
        val vxReduced = vx.fold((vMin.. vMax).toSet()) { acc, vSet -> acc intersect vSet }
        val vyReduced = vy.fold((vMin.. vMax).toSet()) { acc, vSet -> acc intersect vSet }
        val vzReduced = vz.fold((vMin.. vMax).toSet()) { acc, vSet -> acc intersect vSet }
        // TODO: further prune v*Reduced in case there are more 1 possible speeds in the set - fix the below 3 lines
        val vXThrow = vxReduced.last()
        val vYThrow = vyReduced.last()
        val vZThrow = vzReduced.last()
        // step 2 calculate position
        // TODO: refactor the position calculation to make it more robust
        var xThrow: Long? = null
        var yThrow: Long? = null
        var zThrow: Long? = null
        outerLoop1@for (i in 0 .. stones.lastIndex-1)
            for (j in i+1 .. stones.lastIndex) {
                xThrow = calculatePossiblePosThrow(i, j, vXThrow, 0)
                if (xThrow != null)
                    break@outerLoop1
            }
        outerLoop1@for (i in 0 .. stones.lastIndex-1)
            for (j in i+1 .. stones.lastIndex) {
                yThrow = calculatePossiblePosThrow(i, j, vYThrow, 1)
                if (yThrow != null)
                    break@outerLoop1
            }
        outerLoop1@for (i in 0 .. stones.lastIndex-1)
            for (j in i+1 .. stones.lastIndex) {
                zThrow = calculatePossiblePosThrow(i, j, vZThrow, 2)
                if (yThrow != null)
                    break@outerLoop1
            }
        return Stone(Point3DL(xThrow!!, yThrow!!, zThrow!!), Point3DL(vXThrow, vYThrow, vZThrow))
    }

    fun calculatePossibbleVThrow(pList: Pair<Long,Long>, v: Long): Set<Long> {
        val result = mutableSetOf<Long>()
        for (vThrow in vMin .. vMax)
            if (vThrow != v && (pList.first - pList.second) % (vThrow - v) == 0L)
                result.add(vThrow)
        return result
    }

    fun calculatePossiblePosThrow(i1: Int, i2: Int, vThrow: Long, xyz: Int): Long? {
        val (position, time) = LinearEqSys.solve2(
            longArrayOf(1, 1),
            longArrayOf(vThrow - stones[i1].velocity.toList()[xyz], vThrow - stones[i2].velocity.toList()[xyz]),
            longArrayOf(stones[i1].position.toList()[xyz], stones[i2].position.toList()[xyz])
        )
        if (position.isNaN() || ceil(position).toLong() != position.toLong() || ceil(time).toLong() != time.toLong() || time <= 0.0)
            return null
        // check the result is acceptable for all stones
        (stones.indices).forEach { i ->
            if (vThrow == stones[i].velocity.toList()[xyz])
                return@forEach
            if ((stones[i].position.toList()[xyz] - position.toLong()) % (vThrow - stones[i].velocity.toList()[xyz]) != 0L
                || (stones[i].position.toList()[xyz] - position.toLong()) % (vThrow - stones[i].velocity.toList()[xyz]) <= 0L)
                return null
        }
        return position.toLong()
    }
}

data class Stone(var position: Point3DL = Point3DL(0,0,0), var velocity: Point3DL = Point3DL(0,0,0)) {
    fun posXYForTime(t: Double) = Pair(
        position.x + velocity.x * t, position.y + velocity.y * t
    )

    fun timesForXYIntersect(other: Stone): Pair<Double, Double> = LinearEqSys.solve2(
        longArrayOf(velocity.x, velocity.y), longArrayOf(-other.velocity.x, -other.velocity.y),
        longArrayOf(other.position.x - position.x, other.position.y - position.y)
    )

}