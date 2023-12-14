package mpdev.springboot.aoc2023.utils

import kotlin.math.abs

data class Point(var x: Int, var y: Int): Comparable<Point> {
    constructor(p: Point): this(p.x, p.y)
    override fun compareTo(other: Point): Int {
        // order is from top to bottom and from left to right
        // with y increasing downwards and x increasing to the right
        if (this.y == other.y)
            return this.x.compareTo(other.x)
        else
            return this.y.compareTo(other.y)
    }
    operator fun plus(other: Point) =
        Point(this.x + other.x, this.y + other.y)

    operator fun times(n: Int) =
        Point(n * this.x, n * this.y)

    operator fun minus(other: Point) =
        Point(this.x - other.x, this.y - other.y)

    fun adjacent(diagonally: Boolean = true): Array<Point> =
        (if (diagonally)
            listOf(
                Point(x-1,y), Point(x-1,y-1), Point(x,y-1), Point(x+1,y-1),
                Point(x+1,y), Point(x+1,y+1), Point(x,y+1), Point(x-1,y+1)
            )
        else
            listOf(Point(x-1,y), Point(x,y-1), Point(x+1,y), Point(x,y+1)))
            .toTypedArray()

    fun adjacentCardinal() = adjacent(false)

    fun manhattan(other: Point): Int =
        abs(this.x - other.x) + abs(this.y - other.y)
    override fun toString() = "Point($x,$y)"

    class ComparatorXY: Comparator<Point> {
        override fun compare(p1: Point?, p2: Point?): Int {
            if (p1 == null || p2 == null)
                throw AocException ("unknown error - null point")
            if (p1.x < p2.x)
                return -1
            if (p1.x > p2.x)
                return 1
            return Integer.compare(p1.y, p2.y)
        }

    }
}