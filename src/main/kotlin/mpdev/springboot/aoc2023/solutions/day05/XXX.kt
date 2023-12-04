package mpdev.springboot.aoc2023.solutions.day05

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class XXX(input: List<String>) {

    val xyz = Json.decodeFromString<Map<Int,YYY>>(
       input.joinToString(",", "{", "}") { it.toJson() }
    )

    companion object {
        fun String.toJson() =
            // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            // 1:{"winning":[41,48,83,86,17],"numbers":[83,86,6,31,17,9,48,53]}
            this.replace(Regex("""Card +"""), """""")
                .replace(Regex(" {2}"), """ """)
                .replace(": ", """:{"winning":[""")
                .replace(Regex("""$"""), """]}""")
                .replace(" | ", """],"numbers":[""")
                .replace(Regex(" "), """,""")
    }
}

@Serializable
data class YYY(val x1: Int, val x2: Int)