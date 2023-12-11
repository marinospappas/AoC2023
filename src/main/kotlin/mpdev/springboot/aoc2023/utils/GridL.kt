package mpdev.springboot.aoc2023.utils

open class GridL<T>(inputGridVisual: List<String> = emptyList(),
                    private val mapper: Map<Char,T>,
                    private val border: Int = 1,
                    private val defaultChar: Char = '.',
                    private val defaultSize: Pair<Long,Long> = Pair(-1,-1)) {

    protected var data = mutableMapOf<PointL,T>()
    protected var maxX: Long = 0
    protected var maxY: Long = 0
    protected var minX: Long = 0
    protected var minY: Long = 0
    protected var DEFAULT_CHAR: Char

    init {
        if (inputGridVisual.isNotEmpty()) {
            processInputVisual(inputGridVisual)
            updateXYDimensions(border)
        }
        DEFAULT_CHAR = defaultChar
    }

    constructor(gridData: Map<PointL,T>, mapper: Map<Char,T>, border: Int = 1, defaultChar: Char = '.', defaultSize: Pair<Long,Long> = Pair(-1,-1)):
            this(mapper = mapper, border = border, defaultChar = defaultChar, defaultSize = defaultSize) {
        data = gridData.toMutableMap()
        updateXYDimensions(border)
    }

    constructor(inputGridXY: Set<String>, mapper: Map<Char,T>, border: Int = 1, defaultChar: Char = '.', defaultSize: Pair<Long,Long> = Pair(-1,-1)):
            this(mapper = mapper, border = border, defaultChar = defaultChar, defaultSize = defaultSize) {
        processInputXY(inputGridXY)
        updateXYDimensions(border)
    }

    constructor(inputGridXY: Array<PointL>, mapper: Map<Char,T>, border: Int = 1, defaultChar: Char = '.', defaultSize: Pair<Long,Long> = Pair(-1,-1)):
            this(mapper = mapper, border = border, defaultChar = defaultChar, defaultSize = defaultSize) {
        processInputXY(inputGridXY)
        updateXYDimensions(border)
    }

    private fun updateXYDimensions(border: Int) {
        if (defaultSize.first > 0 && defaultSize.second > 0) {
            minX = 0
            maxX = defaultSize.first - 1
            minX = 0
            maxY = defaultSize.second - 1
        }
        else if (data.isNotEmpty()) {
            maxX = data.keys.maxOf { it.x } + border
            maxY = data.keys.maxOf { it.y } + border
            minX = data.keys.minOf { it.x } - border
            minY = data.keys.minOf { it.y } - border
        }
        else {
            maxX = 0; maxY = 0; minX = 0; minY = 0
        }
    }

    fun getDataPoints() = data.toMap()
    open fun getDataPoint(p: PointL) = data[p]
    open fun setDataPoint(p: PointL, t: T) {
        data[p] = t
    }
    open fun removeDataPoint(p: PointL) {
        data.remove(p)
    }

    fun getAdjacentArea(p: PointL): Set<PointL> {
        val area = mutableSetOf<PointL>()
        val value = data[p] ?: return area
        val visited = mutableSetOf<PointL>()
        val queue = ArrayDeque<PointL>().also { q -> q.add(p) }
        while (queue.isNotEmpty()) {
            val point = queue.removeFirst().also { visited.add(it) }
            area.add(point)
            point.adjacent(false).filter { data[it] == value }.forEach { adj ->
                if (!visited.contains(adj))
                    queue.add(adj)
            }
        }
        return area
    }

    fun getDimensions() = Pair(maxX-minX+1, maxY-minY+1)
    fun getMinMaxXY() = FourComponentsL(minX, maxX, minY, maxY)
    fun countOf(item: T) = data.values.count { it == item }

    fun firstPoint() = PointL(minX,minY)

    fun nextPoint(p: PointL) = if (p.x < maxX) p + PointL(1,0) else PointL(minX,p.y+1)

    open fun isInsideGrid(p: PointL) = p.x in minX..maxX && p.y in minY..maxY
    open fun isOnEdge(p: PointL) = p.x == minX || p.x == maxX || p.y == minY || p.y == maxY

    open fun updateDimensions() {
        updateXYDimensions(border)
    }

    companion object {
        val allCharsDefMapper = (' '..'~').associateWith { it }
        private val bitToInt = intArrayOf( 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768,
            65536, 131072, 262144, 524288, 1_048_576, 2_097_152, 4_194_304, 8_388_608,
            16_777_216, 33_554_432, 67_108_864, 134_217_728, 268_435_456, 536_870_912, 1_073_741_824 )
    }

    private fun processInputVisual(input: List<String>) {
        input.indices.forEach { y ->
            input[y].indices.forEach { x ->
                if (mapper[input[y][x]] != null)
                    data[PointL(x.toLong(), y.toLong())] = mapper[input[y][x]]!!
            }
        }
    }

    private fun processInputXY(input: Array<PointL>) {
        input.forEach { p ->
            data[p] = mapper.values.first()
        }
    }

    private fun processInputXY(input: Set<String>) {
        input.forEach { s ->
            val (x, y) = s.split(",")
            data[PointL(x.trim().toLong(), y.trim().toLong())] = mapper.values.first()
        }
    }

    private fun data2Grid(): Array<CharArray> {
        val grid: Array<CharArray> = Array((maxY-minY+1).toInt()) { CharArray((maxX-minX+1).toInt()) { DEFAULT_CHAR } }
        data.forEach { (pos, item) -> grid[(pos.y - minY).toInt()][(pos.x - minX).toInt()] = map2Char(item) }
        return grid
    }

    protected fun map2Char(t: T) = mapper.entries.firstOrNull { e -> e.value == t }?.key ?:
    if (t is Int) '0' + t%10 else 'x'

    open fun print() {
        printGrid(data2Grid())
    }

    protected fun printGrid(grid: Array<CharArray>) {
        for (i in grid.indices) {
            print("${String.format("%2d",i%100)} ")
            for (j in grid.first().indices)
                print(grid[i][j])
            println("")
        }
        print("   ")
        for (i in grid.first().indices)
            print(if (i%10 == 0) (i/10)%10 else " ")
        println("")
        print("   ")
        for (i in grid.first().indices)
            print(i%10)
        println("")
    }

}

data class FourComponentsL(val x1: Long, val x2: Long, val x3: Long, val x4: Long)