package mpdev.springboot.aoc2023.utils

import java.util.Collections.swap

object AocUtils {

    fun permutations(input: MutableList<Int>): List<List<Int>> {
        val perms = mutableListOf<List<Int>>()
        val n = input.size
        val indexes = IntArray(n)
        for (i in 0 until n) {
            indexes[i] = 0
        }
        perms.add(mutableListOf<Int>().also { it.addAll(input) })
        var i = 0
        while (i < n) {
            if (indexes[i] < i) {
                swap(input, if (i % 2 == 0) 0 else indexes[i], i)
                perms.add(mutableListOf<Int>().also { it.addAll(input) })
                indexes[i]++
                i = 0
            } else {
                indexes[i] = 0
                i++
            }
        }
        return perms
    }
}