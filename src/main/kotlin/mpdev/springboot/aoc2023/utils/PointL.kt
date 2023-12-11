package mpdev.springboot.aoc2023.utils

import kotlin.math.abs

data class PointL(var x: Long, var y: Long): Comparable<PointL> {
    constructor(p: PointL): this(p.x, p.y)
    override fun compareTo(other: PointL): Int {
        // order is from top to bottom and from left to right
        // with y increasing downwards and x increasing to the right
        if (this.y == other.y)
            return this.x.compareTo(other.x)
        else
            return this.y.compareTo(other.y)
    }
    operator fun plus(other: PointL) =
        PointL(this.x + other.x, this.y + other.y)

    operator fun times(n: Int) =
        PointL(n * this.x, n * this.y)

    operator fun minus(other: PointL) =
        PointL(this.x - other.x, this.y - other.y)

    fun adjacent(diagonally: Boolean = true): Array<PointL> =
        (if (diagonally)
            listOf(
                PointL(x-1,y), PointL(x-1,y-1), PointL(x,y-1), PointL(x+1,y-1),
                PointL(x+1,y), PointL(x+1,y+1), PointL(x,y+1), PointL(x-1,y+1)
            )
        else
            listOf(PointL(x-1,y), PointL(x,y-1), PointL(x+1,y), PointL(x,y+1)))
            .toTypedArray()

    fun adjacentCardinal() = adjacent(false)

    fun manhattan(other: PointL): Long =
        abs(this.x - other.x) + abs(this.y - other.y)
    override fun toString() = "Point($x,$y)"
}