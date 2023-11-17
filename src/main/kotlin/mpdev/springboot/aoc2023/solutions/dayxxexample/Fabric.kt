package mpdev.springboot.aoc2023.solutions.dayxxexample

import mpdev.springboot.aoc2023.utils.AocException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.Point
import kotlin.math.min
import kotlin.math.max

class Fabric(input: List<String>) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    val claims = mutableMapOf<Int, Claim>()

    init {
        processInput(input)
    }

    fun findOverlappingPoints(): Set<Point> {
        log.info("starting overlapping calculation")
        val overlappingList = mutableListOf<Set<Point>>()
        val claimsData = claims.values.toList()
        for (i in 0 .. claimsData.lastIndex-1)
            for (j in i+1 .. claimsData.lastIndex) {
                val overlap = overlappingPoints(claimsData[i], claimsData[j])
                if (overlap.isNotEmpty())
                    overlappingList.add(overlap)
            }
        return mutableSetOf<Point>().also { set -> overlappingList.forEach { set.addAll(it) } }
    }

    fun findNonOverlappingClaim(): Int {
        val claimsData = claims.values.toList()
        for (i in 0 .. claimsData.lastIndex) {
            var overlaps = false
            for (j in 0..claimsData.lastIndex) {
                if (i == j)
                    continue
                val overlap = overlappingPoints(claimsData[i], claimsData[j])
                if (overlap.isNotEmpty()) {
                    overlaps = true
                    break
                }
            }
            if (!overlaps)
                return claims.keys.toList()[i]
        }
        return -1
    }

    private fun overlappingPoints(claim1: Claim, claim2: Claim): Set<Point> {
        val overlapping = mutableSetOf<Point>()
        val x01 = claim1.x1
        val y01 = claim1.y1
        val x02 = claim1.x2
        val y02 = claim1.y2
        val x11 = claim2.x1
        val y11 = claim2.y1
        val x12 = claim2.x2
        val y12 = claim2.y2
        val xOverlap = min(x02,x12) - max(x01,x11)
        val yOverlap = min(y02,y12) - max(y01,y11)
        if (xOverlap > 0 && yOverlap > 0) {
            for (x in max(x01,x11) until min(x02,x12))
                for (y in max(y01,y11) until min(y02,y12))
                    overlapping.add(Point(x,y))
        }
        return overlapping
    }

    private fun processInput(input: List<String>) {
        input.forEach { line -> createClaim(line) }
    }

    private fun createClaim(s: String) {
        // #1 @ 1,3: 4x4
        val match = Regex("""#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""").find(s)
        try {
            val (id, x0, y0, width, height) = match!!.destructured
            claims[id.toInt()] = Claim(x0.toInt(), y0.toInt(), x0.toInt()+width.toInt(), y0.toInt()+height.toInt())
        } catch (e: Exception) {
            throw AocException("bad input line $s")
        }
    }

}

data class Claim(val x1: Int, val y1: Int, val x2: Int, val y2: Int)