package mpdev.springboot.aoc2023.solutions.day24

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.*

@Serializable
@AocInClass(delimiters = ["~"])
data class AoCInput(
    // 2,0,5~2,2,5
    // 0     1
    @AocInField(0, delimiters = [","]) val coord1: List<Int>,
    @AocInField(1, delimiters = [","]) val coord2: List<Int>
)

class Xxxx(input: List<String>) {

    var debug = false
    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)

}

