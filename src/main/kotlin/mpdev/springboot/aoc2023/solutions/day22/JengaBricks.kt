package mpdev.springboot.aoc2023.solutions.day22

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.*
import kotlin.math.abs

@Serializable
@AocInClass(delimiters = ["~"])
data class AoCInput(
    // 2,0,5~2,2,5
    // 0     1
    @AocInField(0, delimiters = [","]) val coord1: List<Int>,
    @AocInField(1, delimiters = [","]) val coord2: List<Int>
)

class JengaBricks(input: List<String>) {

    // TODO refactor this class to make it less verbose

    var debug = false
    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    val bricks = aocInputList.sortedBy { it.coord1[2] }
        .map {
            val pointsSet = mutableSetOf<PointND>()
            for (x in it.coord1[0] .. it.coord2[0])
                for (y in it.coord1[1] .. it.coord2[1])
                    for (z in it.coord1[2] .. it.coord2[2])
                        pointsSet.add(PointND(intArrayOf(x, y, z)))
            Brick(pointsSet)
        }
    private val space: MutableMap<PointND, Int> = mutableMapOf()

    fun getRemovableBricks(): Set<Int> =
        // a brick can be removed if (a) it supports nothing or
        // (b) all the bricks supported by it have at least one more support
        bricks.indices.filter { id ->
            val brick = bricks[id]
            brick.supports.isEmpty() || brick.supports.all { bricks[it].supportedBy.size >= 2 }
        }.toSet()

    fun landAllBricks() {
        for (id in bricks.indices) {
            val brick = bricks[id]
            landBrick(id, brick)
            if (debug) {
                println("landed brick $brick")
                space.forEach { it.println() }
                bricks.forEach { it.println() }
            }
        }
    }

    private fun landBrick(id: Int, brick: Brick) {
        val groundPoints = brick.points.map { Point(it.x(), it.y()) }
        val spacePointsSection = space.keys.filter { groundPoints.contains(Point(it.x(), it.y())) }
        val highestPoint = spacePointsSection.maxOfOrNull { it.z() } ?: -1
        // get any bricks immediately below and add them to the supported by list
        // also update the "supports" lists of the bricks below
        if (highestPoint >= 0)   // only if it lands on another brick
            spacePointsSection.forEach { p3d ->
                if (p3d.z() == highestPoint) {
                    val supportingBrickId = space[p3d]!!
                    bricks[id].supportedBy.add(supportingBrickId)
                    bricks[supportingBrickId].supports.add(id)
                }
            }
        // brick lands on top
        val lowZ = brick.points.first().z()
        brick.points.forEach { p ->
            val newP = PointND(intArrayOf(p.x(), p.y(), highestPoint + 1 + p.z() - lowZ))
            space[newP] = id
        }
    }

    fun determineBricksToFall(brickId: Int): Int {
        val jenga = bricks.indices.toMutableList().also { j -> j.remove(brickId) }
        val fallen = mutableListOf(brickId)
        do {
            val unsupported = jenga.filter {
                bricks[it].supportedBy.isNotEmpty() && bricks[it].supportedBy.all { supportId ->
                    fallen.contains(supportId)
                }
            }
            jenga.removeAll(unsupported)
            fallen.addAll(unsupported)
        } while (unsupported.isNotEmpty())
        return fallen.size - 1
    }
}

data class Brick(val points: Set<PointND>,
                 val supports: MutableSet<Int> = mutableSetOf(),
                 val supportedBy: MutableSet<Int> = mutableSetOf()
)
