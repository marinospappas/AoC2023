package mpdev.springboot.aoc2023.utils

import kotlin.math.sqrt

class QuadraticEq {

    companion object {
        fun solve(a: Int, b: Int, c: Int): Pair<Double,Double> =
            solve(a.toLong(), b.toLong(), c.toLong())

        fun solve(a: Long, b: Long, c: Long): Pair<Double,Double> {
            // b squared - 4 a c
            val d = sqrt((b * b - 4 * a * c).toDouble())
            val x1 = (-b - d) / (2 * a)
            val x2 = (-b + d) / (2 * a)
            return Pair(x1, x2)
        }

        fun solve(a: Double, b: Double, c: Double): Pair<Double,Double> {
            // b squared - 4 a c
            val d = sqrt(b * b - 4 * a * c)
            val x1 = (-b - d) / (2 * a)
            val x2 = (-b + d) / (2 * a)
            return Pair(x1, x2)
        }
    }
}