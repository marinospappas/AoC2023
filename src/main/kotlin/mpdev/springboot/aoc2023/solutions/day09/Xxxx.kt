package mpdev.springboot.aoc2023.solutions.day09

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mpdev.springboot.aoc2023.utils.AocException
import mpdev.springboot.aoc2023.utils.InputClass
import mpdev.springboot.aoc2023.utils.InputField
import mpdev.springboot.aoc2023.utils.toJson
import kotlin.math.pow

class Xxxx(input: List<String>) {

    //val cardsList = Json.decodeFromString<List<ScratchCard>>(
    //    input.joinToString(",", "[", "]") {  it.toJson(ScratchCard::class.java) }
    //)

}

@Serializable
@InputClass("Card", [":", "\\|"])
data class Testxxx(
    // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
    //      0  1                1
    @InputField(0) val field0: String,
    @InputField(1, " +") val winning: List<Int>)