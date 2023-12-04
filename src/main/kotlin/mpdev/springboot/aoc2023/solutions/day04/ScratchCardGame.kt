package mpdev.springboot.aoc2023.solutions.day04

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mpdev.springboot.aoc2023.utils.AocException
import kotlin.math.pow

class ScratchCardGame(input: List<String>) {

    val cards = Json.decodeFromString<Map<Int,ScratchCard>>(
       input.joinToString(",", "{", "}") { it.toJson() }
    )

    fun playGamePart1() = cards.values.sumOf { c -> c.points() }

    fun playGamePart2(): Int {
        val cardsList = cards.entries.map { Pair(it.key,it.value) }.toMutableList()
        while (true) {
            val wonList = mutableListOf<Pair<Int,ScratchCard>>()
            cardsList.filter { !it.second.processed }.forEach { card ->
                val winning = card.second.countOfWinningNums()
                if (winning > 0) {
                    for (i in card.first+1 .. card.first+winning) {
                        val cardWon = cards[i] ?: throw AocException("error for card id $i")
                        wonList.add(Pair(i,ScratchCard(cardWon.winning,cardWon.numbers)))
                    }
                }
                card.second.processed = true
            }
            cardsList.addAll(wonList)
            if (cardsList.all { it.second.processed })
                break
        }
        return cardsList.size
    }

    companion object {
        fun String.toJson() =
            // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            //
            this
                .replace(Regex("""Card +"""), """""")
                .replace(Regex(" {2}"), """ """)
                .replace(": ", """:{"winning":[""")
                .replace(Regex("""$"""), """]}""")
                .replace(" | ", """],"numbers":[""")
                .replace(Regex(" "), """,""")
    }
}

@Serializable
data class ScratchCard(val winning: List<Int>, val numbers: List<Int>, var processed: Boolean = false) {
    fun countOfWinningNums() = (winning.toSet() intersect numbers.toSet()).size
    fun points(): Int {
        val countOfWinning = countOfWinningNums()
        return if (countOfWinning > 0) 2.toDouble().pow(countOfWinningNums() - 1).toInt()
        else 0
    }
}