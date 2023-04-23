package mpdev.springboot.aoc2023.utils

object AoCUtils {

    val indexMap: Map<Int,Int> = mapOf(
        1 to 0,
        2 to 1
    )

    fun getIndexOfDay(day: Int) = indexMap[day] ?: (day - 1)
}