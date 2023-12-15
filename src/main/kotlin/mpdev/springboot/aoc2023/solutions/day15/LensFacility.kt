package mpdev.springboot.aoc2023.solutions.day15

class LensFacility(input: List<String>) {

    val initInstructions: List<String> = input[0].split(",")
    val boxes = Array(256) { mutableMapOf<String,Int>() }

    fun boxFocalPwr(box: Map<String,Int>): Int {
        var slot = 1
        return box.values.fold(0) { acc, n -> acc + n * slot++ }
    }

    fun executeStep(s: String) {
        val sArray = s.split(Regex("""[-=]"""))
        val label = sArray[0]
        val boxId = label.hashD152023()
        val box = boxes[boxId]
        if (s.contains('=')) {
            val focalPt = Integer.parseInt(sArray[1])
            box[label] = focalPt
        }
        else
            box.remove(label)
    }
}

fun String.hashD152023(): Int {
    var hash = 0
    this.forEach { c ->
        hash += c.code
        hash *= 17
        hash %= 256
    }
    return hash
}