package mpdev.springboot.aoc2023.solutions.day22

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.*

@Serializable
@AocInClass(delimiters = ["->"])
data class AoCInput(
    // broadcaster -> a
    // %a -> inv, con
    // 0     1    2
    @AocInField(0) val sender: String,
    @AocInField(1, delimiters = [","]) val receivers: List<String>
)

class Xxxx(input: List<String>) {

    var debug = false
    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)

}