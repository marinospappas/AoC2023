package mpdev.springboot.aoc2023.utils

class CircularList<T>(val data: MutableList<T>) {

    var curPos = 0

    fun size() = data.size

    operator fun get(index: Int) = data[index]
    operator fun set(index: Int, value: T) {
        data[index] = value
    }

    fun indexOf(value: T) = data.indexOf(value)

    fun getCurrent() = data[curPos]

    fun insert(index: Int, value: T) {
        data.add(index, value)
        curPos = index
    }

    fun incrCurPos(incr: Int) {
        curPos = (curPos + incr) % data.size
    }

    fun sublist(length: Int): List<T> {
        val sList = mutableListOf<T>()
        var index = curPos
        for (i in 1..length) {
            sList.add(data[index])
            if (++index > data.lastIndex)
                index = 0
        }
        return sList
    }

    fun toList() = data

    fun setSubList(sList: List<T>) {
        var index = curPos
        for (i in 0..sList.lastIndex) {
            data[index] = sList[i]
            if (++index > data.lastIndex)
                index = 0
        }
    }

    override fun toString() = data.toString()
}