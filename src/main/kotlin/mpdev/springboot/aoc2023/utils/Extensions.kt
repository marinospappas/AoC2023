package mpdev.springboot.aoc2023.utils

import java.lang.StringBuilder
import kotlin.math.abs
import kotlin.math.min

fun String.splitRepeatedChars(): List<String> {
    if (isEmpty())
        return emptyList()
    val s = StringBuilder(this)
    var index = 0
    val delimiter = '_'
    var previous = s.first()
    while (index < s.length) {
        if (s[index] != previous)
            s.insert(index++, delimiter)
        previous = s[index]
        ++index
    }
    return s.split(delimiter)
}

fun Int.lastDigit() = this % 10

fun Int.numOfDigits() = abs(this).toString().length

operator fun IntArray.plus(other: IntArray) = Array(size) { this[it] + other[it] }.toIntArray()

fun Array<IntRange>.allValues(): Set<MutableList<Int>> {
    return if (size == 1)
        (0 .. this[0].last - this[0].first).map { mutableListOf(this[0].first + it) }.toSet()
    else {
        val result = mutableSetOf<MutableList<Int>>()
        this.last().forEach { value ->
            this.sliceArray(0 .. size - 2).allValues().forEach { combo ->
                result.add(combo.also { combo.add(value) })
            }
        }
        result
    }
}

fun min(a1: Int, a2: Int, a3: Int, a4: Int): Int {
    return min(min(a1,a2), min(a3,a4))
}

fun min(a1: Long, a2: Long, a3: Long, a4: Long): Long {
    return min(min(a1,a2), min(a3,a4))
}

fun String.isAnagram(other: String): Boolean =
    this.toCharArray().toList().sorted() == other.toCharArray().toList().sorted()