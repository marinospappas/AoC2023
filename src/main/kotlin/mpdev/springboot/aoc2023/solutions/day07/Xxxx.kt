package mpdev.springboot.aoc2023.solutions.day07

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mpdev.springboot.aoc2023.utils.InputClass
import mpdev.springboot.aoc2023.utils.InputField
import mpdev.springboot.aoc2023.utils.toJson

class Xxxx(input: List<String>) {

    val inputMappedList = Json.decodeFromString<List<InputMapped>>(
        input.joinToString(",", "[", "]") {  it.toJson(InputMapped::class.java) }
    )

}

@Serializable
@InputClass("Card", [":", "\\|"])
data class InputMapped(
    // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
    //      0  1                1
    @InputField(0) val field1: Int,
    @InputField(1) val field2: Int,
    @InputField(2) val field3: Int,
    @InputField(3) val field4: Int,
    @InputField(4, " +") val field5: List<Int>,
    @InputField(5, " +") val field6: List<Int>,
    var processed: Boolean = false, var winningCount: Int = -1)