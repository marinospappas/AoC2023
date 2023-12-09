package mpdev.springboot.aoc2023.solutions.day10

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.InputClass
import mpdev.springboot.aoc2023.utils.InputField
import mpdev.springboot.aoc2023.utils.InputUtils

@Serializable
@InputClass
data class AoCInput(
    // 0 3 6 9 12 15
    // 0
    @InputField(0, delimiters = [" "]) val values: List<Long>,
    @InputField(1) val field1: Int
)

class Xxxx(input: List<String>) {

    //private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    //val failures: List<List<Long>> = aocInputList.map { it.values }


}