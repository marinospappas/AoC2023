package mpdev.springboot.aoc2023.utils

/**
 * N-dimension Grid uses N-dimension Point
 */
class GridND<T>(input: List<String>, private val mapper: Map<Char,T>, private val dimensions: Int = 2) {

    companion object {
        const val DEFAULT_CHAR = '.'
    }

    private var data = mutableMapOf<PointND,T>()
    private var maxX_i = IntArray(dimensions) { 0 }
    private var minX_i = IntArray(dimensions) { 0 }

    init {
        processInput2D(input)
    }

    fun getData() = data.toMap()
    fun getDataPoint(p: PointND) = data[p]
    fun setDataPoint(p: PointND, t: T) {
        data[p] = t
    }
    fun getDimensions() = IntArray(dimensions) { maxX_i[it]-minX_i[it]+1 }
    fun countOf(item: T) = data.values.count { it == item }

    fun getAllPointsInGrid(): Set<PointND> {
        val ranges = Array(dimensions) { IntRange(minX_i[it], maxX_i[it]) }
        return ranges.allValues().map { PointND(it.toIntArray()) }.toSet()
    }

    fun expand(count: Int) {
        (0 until dimensions).forEach { minX_i[it] -= count; maxX_i[it] += count }
    }

    //////////////////////////////////////////////////////////

    private fun processInput2D(input: List<String>) {
        input.indices.forEach { y ->
            input[y].indices.forEach { x ->
                if (mapper[input[y][x]] != null)
                    data[PointND(x, y, dimensions)] = mapper[input[y][x]]!!
            }
        }
        maxX_i[0] = input.first().length - 1
        maxX_i[1] = input.size - 1
    }

    private fun map2Char(t: T) = mapper.entries.first { e -> e.value == t }.key

    fun print() {
        if (dimensions == 2) {
            printGrid2D(data2Grid2D(intArrayOf()))
        }
        else {
            val addnlRanges = Array(dimensions - 2) { IntRange(minX_i[it+2], maxX_i[it+2]) }
            addnlRanges.allValues().forEach { dim ->
                println("coord(i): $dim")
                printGrid2D(data2Grid2D(dim.toIntArray()))
            }
        }
    }

    private fun data2Grid2D(a: IntArray): Array<CharArray> {
        val grid: Array<CharArray> = Array(maxX_i[1]-minX_i[1]+1) { CharArray(maxX_i[0]-minX_i[0]+1) { DEFAULT_CHAR } }
        val thisData = if (a.isEmpty()) data else data.filter { it.key.x_i.sliceArray(2 until dimensions).contentEquals(a) }
        thisData.forEach { (pos, item) -> grid[pos.y()-minX_i[1]][pos.x()-minX_i[0]] = map2Char(item) }
        return grid
    }

    private fun printGrid2D(grid: Array<CharArray>) {
        for (i in grid.indices) {
            print("${String.format("%2d",i)} ")
            for (j in grid.first().indices)
                print(grid[i][j])
            println()
        }
        print("   ")
        for (i in grid.first().indices)
            print(if (i%10 == 0) i/10 else " ")
        println()
        print("   ")
        for (i in grid.first().indices)
            print(i%10)
        println()
    }
}