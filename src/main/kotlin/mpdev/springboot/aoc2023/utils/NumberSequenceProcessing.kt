package mpdev.springboot.aoc2023.utils

// intList must be sorted
fun get2SumComponents(intList: List<Int>, sum: Int, sorted: Boolean = true): Pair<Int, Int> {
    val size = intList.size
    val numberArray = if (sorted) intList.toIntArray() else intList.sorted().toIntArray()
    var mid = -1
    val halfSum = sum / 2
    for (i in numberArray.indices)
        if (numberArray[i] > halfSum) {
            mid = i
            break
        }
    if (mid <= 0)
        return Pair(-1, -1)
    for (i in 0..mid)
        for (j in size - 1 downTo mid - 1) {
            if (numberArray[i] + numberArray[j] == sum)
                return Pair(numberArray[i], numberArray[j])
        }
    return Pair(-1, -1)
}

// intList must be sorted
fun get2SumComponents(intList: List<Long>, sum: Long, sorted: Boolean = true): Pair<Long, Long> {
    val size = intList.size
    val numberArray = if (sorted) intList.toLongArray() else intList.sorted().toLongArray()
    var mid = -1
    val halfSum = sum / 2
    for (i in numberArray.indices)
        if (numberArray[i] > halfSum) {
            mid = i
            break
        }
    if (mid <= 0)
        return Pair(-1, -1)
    for (i in 0..mid)
        for (j in size - 1 downTo mid - 1) {
            if (numberArray[i] + numberArray[j] == sum)
                return Pair(numberArray[i], numberArray[j])
        }
    return Pair(-1, -1)
}
