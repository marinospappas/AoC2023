package mpdev.springboot.aoc2023.utils

import kotlin.math.abs

data class Point3DL(var x: Long, var y: Long, var z: Long): Comparable<Point3DL> {
    constructor(p: Point3DL): this(p.x, p.y, p.z)
    constructor(l: List<Long>): this(l[0], l[1], l[2])

    //TODO: complete the below
    override fun compareTo(other: Point3DL): Int {
        // order is from top to bottom and from left to right
        // with y increasing downwards and x increasing to the right
        if (this.y == other.y)
            return this.x.compareTo(other.x)
        else
            return this.y.compareTo(other.y)
    }

    operator fun get(i: Int) = when(i) { 0-> x; 1-> y; else-> z }

    operator fun plus(other: Point3DL) =
        Point3DL(this.x + other.x, this.y + other.y, this.z + other.z)

    operator fun times(n: Int) =
        Point3DL(n * this.x, n * this.y, n * this.z)

    operator fun minus(other: Point3DL) =
        Point3DL(this.x - other.x, this.y - other.y, this.z - other.z)

    //TODO: complete the below
    fun adjacent(diagonally: Boolean = true): Array<Point3DL> =
        (if (diagonally)
            listOf(
                Point3DL(x-1,y,z), Point3DL(x-1,y-1,z), Point3DL(x,y-1,z), Point3DL(x+1,y-1,z),
                Point3DL(x+1,y,z), Point3DL(x+1,y+1,z), Point3DL(x,y+1,z), Point3DL(x-1,y+1,z)
            )
        else
            listOf(Point3DL(x-1,y,z), Point3DL(x,y-1,z), Point3DL(x+1,y,z), Point3DL(x,y+1,z)))
            .toTypedArray()

    fun adjacentCardinal() = adjacent(false)

    fun manhattan(other: Point3DL): Long =
        abs(this.x - other.x) + abs(this.y - other.y) + abs(this.z - other.z)

    fun toList() = listOf(x, y, z)

    override fun toString() = "Point3D($x,$y,$z)"
}