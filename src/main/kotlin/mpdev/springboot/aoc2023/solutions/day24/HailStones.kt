package mpdev.springboot.aoc2023.solutions.day24

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Serializable
@AocInClass(delimiters = ["@"])
data class AoCInput(
    // 18, 19, 22 @ -1, -1, -2
    // 0            1
    @AocInField(0, delimiters = [","]) val position: List<Long>,
    @AocInField(1, delimiters = [","]) val velocity: List<Long>
)

class HailStones(input: List<String>) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    var debug = false
    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    val stones: List<Stone> = aocInputList.map { Stone(Point3DL(it.position), Point3DL(it.velocity)) }
    var testArea = LongRange(200000000000000, 400000000000000)
    // estimated V range for the throw
    val vMin = stones.minOf { it.velocity.toList().min() } * 2
    val vMax = stones.maxOf { it.velocity.toList().max() } * 2

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

    /**
     * the throw position and speed can be calculated by solving a system of 9 equations with 9 unknowns
     * For each collision
     *   Xt = Xot + Vxt * T1      trajectory of the throw
     *   X1 + Xo1 + Vx1 * T1      trajectory of stone 1
     * if T1 is the time of the collision with stone 1, T2 with stone 2 etc, we have
     *   Xot + Vxt * T1 = Xo1 + Vx1 * T1
     *   Xot + Vxt * T2 = Xo2 + Vx2 * T2
     *   Xot + Vxt * T3 = Xo3 + Vx3 * T1
     * including the same for Y and Z we have a total of 9 equations with 9 unknowns
     * the python code for this is quite simple (using sympy)
     *
     * x = Symbol('x')
     * y = Symbol('y')
     * z = Symbol('z')
     * vx = Symbol('vx')
     * vy = Symbol('vy')
     * vz = Symbol('vz')
     * equations = []
     * t_syms = []
     * # the first 3 stones will gives 9 equations with 9 unknowns
     * for idx,stone in enumerate(stones[:3]):
     *   # vxt is the velocity of the throw, vx is the velocity of the stone
     *   x,y,z,vx,vy,vz = stone
     *
     *   t = Symbol('t'+str(index))
     *   x_eq = x + vx*t - xt - vxt*t
     *   y_eq = y + vy*t - yt - vyt*t
     *   z_eq = z + vz*t - zt - vzt*t
     *   equations.append(x_eq)
     *   equations.append(y_eq)
     *   equations.append(z_eq)
     *   t_syms.append(t)
     * result = solve_poly_system(equations,*([x,y,z,vx,vy,vz]+t_syms))
     * print(result[0][0],result[0][1],result[0][2],result[0][3],result[0][4],result[0][5]) #part 2 answer
     *
     * Instead, the following method has been used to calculate the speed and position of the throw
     */
    fun calculateThrow(): Stone {
        val (vx, vy, vz) = calculateThrowSpeed()
        log.info("calculated speed: {}, {}, {},", vx, vy, vz)
        val (x, y, z) = calculateThrowPosition(vx, vy, vz)
        log.info("calculated position: {}, {}, {},", x, y, z)
        return Stone(Point3DL(x, y, z), Point3DL(vx, vy, vz))
    }

    /**
     * Assumptions:
     * 1. The throw position is on Integer coordinates
     * 2. The speed components of the throw are Integers
     * 3. The collisions happen at Integer points in time
     * then
     * For each collision
     *   Xt = Xot + Vxt * T1      trajectory of the throw
     *   X1 = Xo1 + Vx1 * T1      trajectory of stone 1
     * if T1 is the time of the collision with stone 1 and T2 with stone 2, we have
     *   Xot + Vxt * T1 = Xo1 + Vx1 * T1
     *   Xot + Vxt * T2 = Xo2 + Vx2 * T2
     * We take all the stones that have SAME Vx (i.e. Vx1 = Vx2 above) and by removing Xt we have
     *   Xo1 - Xo2 = (T1 - T2) * (Vxt - Vx12)
     * which means that we need to select those speeds Vxt for which the modulo (Xo1 - Xo2) % (Vxt - Vx12) is 0
     * As all the stone speeds are similar, the throw must also happen at similar speed
     * A range is chosen between 2 * lowest negative speed and 2 * highest positive speed
     */
    private fun calculateThrowSpeed(): Triple<Long,Long,Long> {
        val vxSet = mutableListOf<Set<Long>>()
        val vySet = mutableListOf<Set<Long>>()
        val vzSet = mutableListOf<Set<Long>>()
        for (i in 0 .. stones.lastIndex-1)
            for (j in i+1 .. stones.lastIndex) {
                if (stones[i].velocity.x == stones[j].velocity.x)
                    vxSet.add(calculatePossibbleVThrow(Pair(stones[i].position.x, stones[j].position.x), stones[i].velocity.x))
                if (stones[i].velocity.y == stones[j].velocity.y)
                    vySet.add(calculatePossibbleVThrow(Pair(stones[i].position.y, stones[j].position.y), stones[i].velocity.y))
                if (stones[i].velocity.z == stones[j].velocity.z)
                    vzSet.add(calculatePossibbleVThrow(Pair(stones[i].position.z, stones[j].position.z), stones[i].velocity.z))
            }
        // take the speed(s) that are common to every entry - if more than 1 (as in test) use the last one
        // ideally this should be further analysed but the real data gives us only 1
        val vx = vxSet.fold((vMin.. vMax).toSet()) { acc, vSet -> acc intersect vSet }.last()
        val vy = vySet.fold((vMin.. vMax).toSet()) { acc, vSet -> acc intersect vSet }.last()
        val vz = vzSet.fold((vMin.. vMax).toSet()) { acc, vSet -> acc intersect vSet }.last()
        return Triple(vx, vy, vz)
    }

    private fun calculatePossibbleVThrow(pList: Pair<Long,Long>, v: Long): Set<Long> {
        val result = mutableSetOf<Long>()
        for (vThrow in vMin .. vMax)
            if (vThrow != v && (pList.first - pList.second) % (vThrow - v) == 0L)
                result.add(vThrow)
        return result
    }

    /**
     * the position is calculated by using the vx and vy velocity components of the throw and the first 2 stones x and y
     * as follows
     * if T1 is the time of the collision with stone 1 and T2 with stone 2, we use the x and y components and have
     *   Xot + Vxt * T1 = Xo1 + Vx1 * T1
     *   Xot + Vxt * T2 = Xo2 + Vx2 * T2   and
     *   Yot + Vyt * T1 = Yo1 + Vy1 * T1
     *   Yot + Vyt * T2 = Yo2 + Vy2 * T2
     * by eliminating Xot and Yot (the x, y of the throw) we get
     *   (Vxt - Vx1) * T1 + (Vx2 - Vxt) * T2 = Xo1 - Xo2
     *   (Vyt - Vy1) * T1 + (Vy2 - Vyt) * T2 = Yo1 - yo2
     * we solve this for T1 and T2 and from T1 and the first equation above we get the x of the throw
     *   Xot = Xo1 + T1 * (Vx1 - Vxt)   and similarly we get the y and z of the throw
     */
    private fun calculateThrowPosition(vx: Long, vy: Long, vz: Long): Triple<Long,Long,Long> {
        val (t1, _) = LinearEqSys.solve2(
            longArrayOf(vx - stones[0].velocity.x, vy - stones[0].velocity.y),
            longArrayOf(stones[1].velocity.x - vx, stones[1].velocity.y - vy),
            longArrayOf(stones[0].position.x - stones[1].position.x, stones[0].position.y - stones[1].position.y),
        )
        val posX = stones[0].position.x + t1.toLong() * (stones[0].velocity.x - vx)
        val posY = stones[0].position.y + t1.toLong() * (stones[0].velocity.y - vy)
        val posZ = stones[0].position.z + t1.toLong() * (stones[0].velocity.z - vz)
        return Triple(posX,posY,posZ)
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