package mpdev.springboot.aoc2023.utils

class LinearEqSys {

    companion object {

        /**
         * solves:
         * a1 * x + b1 * y = c1
         * a2 * x + b2 * y = c2
         */
        fun solve2(a: LongArray, b: LongArray, c: LongArray): Pair<Double,Double> {
            if (a.size != 2 || b.size != 2 || c.size != 2)
                throw AocException("invalid input to solve2 - requires arrays of size 2")
            val d = (b[0] * a[1] - a[0] * b[1]).toDouble().also { if (it == 0.0) return Pair(Double.NaN, Double.NaN) }
            val y = (c[0] * a[1] - a[0] * c[1]) / d
            val x = (b[0] * c[1] - c[0] * b[1]) / d
            return Pair(x,y)
        }
    }
}