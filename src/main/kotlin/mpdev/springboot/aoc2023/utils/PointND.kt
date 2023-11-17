package mpdev.springboot.aoc2023.utils

import kotlin.math.abs

/**
 * N-dimension Point
 * coordinates X(i) in array, the size of the array is the number of dimensions
 */
data class PointND(var x_i: IntArray) {

    private val dimensions = x_i.size

    constructor(x: Int, y: Int, n: Int): this(IntArray(n) {
        when (it) {
            0 -> x
            1 -> y
            else -> 0
        }
    })

    // convenience functions to return the first 3 coordinates for 3 or more-dimension point
    fun x() = x_i[0]
    fun y() = x_i[1]
    fun z() = x_i[2]

    operator fun get(i: Int) = x_i[i]

    operator fun set(i: Int, value: Int) {
        x_i[i] = value
    }

    operator fun plus(other: PointND) = PointND(x_i + other.x_i)

    fun manhattan(other: PointND): Int {
        if (x_i.size != other.x_i.size)
            return Int.MAX_VALUE
        var sum = 0
        for (i in x_i.indices)
            sum += abs(x_i[i] - other.x_i[i])
        return sum
    }

    fun adjacent(): Set<PointND> {
        val ranges = Array(dimensions) { IntRange(-1,1) }
        val allZeroes = Array(dimensions) { 0 }.toList()
        return ranges.allValues().filter { it != allZeroes }.map { this + PointND(it.toIntArray()) }.toSet()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PointND
        if (!x_i.contentEquals(other.x_i)) return false
        if (dimensions != other.dimensions) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x_i.contentHashCode()
        result = 31 * result + dimensions
        return result
    }

    override fun toString() = "Point${x_i.size}D${x_i.toList()}"
}