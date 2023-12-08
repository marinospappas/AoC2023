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

fun Any?.println() = println(this)

fun <T>List<T>.pairWith(other: List<T>): List<Pair<T,T>> {
    val result = mutableListOf<Pair<T,T>>()
    val len = min(this.size, other.size)
    for (indx in 0 until len)
        result.add(Pair(this[indx], other[indx]))
    return result
}

fun Int.divisors(): Set<Int> {
    val result = mutableSetOf<Int>()
    for (i in 1 until this)
        if (this % i == 0)
            result.add(i)
    if (result.size == 1)
        result.add(this)
    return result
}

fun Long.divisors(): Set<Long> {
    val result = mutableSetOf<Long>()
    for (i in 1L until this)
        if (this % i == 0L)
            result.add(i)
    if (result.size == 1)
        result.add(this)
    return result
}

fun Int.isPrime() = this.toLong().isPrime()

fun Long.isPrime(): Boolean {
    val divisors = this.divisors()
    return divisors.size == 2 && divisors.last() == this
}
