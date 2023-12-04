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
        val cardsList = cards.entries.map { Pair(it.key, it.value.clone()) }.toMutableList()
        while (cardsList.any { !it.second.processed }) {
            val wonList = mutableListOf<Pair<Int,ScratchCard>>()
            cardsList.filter { !it.second.processed }.forEach { (id, card) ->
                card.processed = true
                wonList.addAll(getCopiesOfCardsWon(id, card.winningCount))
            }
            cardsList.addAll(wonList)
        }
        return cardsList.size
    }

    private val cacheOfCardsWon = mutableMapOf<Int, List<Pair<Int,ScratchCard>>>()

    fun getCopiesOfCardsWon(id: Int, count: Int) =
        cacheOfCardsWon.getOrPut(id) {
            (id + 1..id + count).map { i -> Pair(i, cards[i] ?: throw AocException("error retrieving card id $i")) }
        }.map { Pair(it.first, it.second.clone()) }

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
data class ScratchCard(val winning: List<Int>, val numbers: List<Int>,
                       var processed: Boolean = false, var winningCount: Int = -1) {
    init {
        if (winningCount < 0)   // calculate the winning count only if it's not set in the constructor
            winningCount = (winning.toSet() intersect numbers.toSet()).size
    }
    fun points() = if (winningCount > 0) 2.toDouble().pow(winningCount - 1).toInt()
    else 0
    fun clone() = ScratchCard(this.winning, this.numbers, false, this.winningCount)
}