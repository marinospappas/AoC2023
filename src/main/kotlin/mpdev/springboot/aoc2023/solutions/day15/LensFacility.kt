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
        val box = boxes[sArray[0].hashD152023()]
        if (s.contains('='))
            box[sArray[0]] = Integer.parseInt(sArray[1])
        else
            box.remove(sArray[0])
    }
}

fun String.hashD152023() = this.toList().fold(0) { hash, c -> (hash + c.code) * 17 % 256 }
