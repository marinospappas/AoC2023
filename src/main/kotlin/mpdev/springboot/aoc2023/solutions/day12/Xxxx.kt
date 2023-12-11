package mpdev.springboot.aoc2023.solutions.day12

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.InputClass
import mpdev.springboot.aoc2023.utils.InputField
import mpdev.springboot.aoc2023.utils.InputUtils

@Serializable
@InputClass
data class AoCInput(
    // 0 3 6 9 12 15
    // 0
    @InputField(0) val id: Int,
    @InputField(1, delimiters = [" "]) val values: List<Long>
)

class Xxxx(input: List<String>) {

    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)

}