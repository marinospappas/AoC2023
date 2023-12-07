package mpdev.springboot.aoc2023.solutions.day08

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

    init {

    }


    companion object {

    }
}

@Serializable
@InputClass("Card", [":", "\\|"])
data class ScratchCard(
    // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
    //      0  1                1
    @InputField(0) val field1: String,
    @InputField(1, " +") val field2: String,
    @InputField(2, " +") val field3: String
    )