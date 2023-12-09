package mpdev.springboot.aoc2023.solutions.day04

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.AocException
import mpdev.springboot.aoc2023.utils.InputClass
import mpdev.springboot.aoc2023.utils.InputField
import mpdev.springboot.aoc2023.utils.InputUtils
import kotlin.math.pow

class ScratchCardGame(input: List<String>) {

    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    private val cardsList: List<ScratchCard> = aocInputList.map { ScratchCard(it.id.toLong().toInt(),it.winning, it.numbers) }
    val cards = cardsList.associateBy { it.id }

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

}

@Serializable
@InputClass(prefix = "Card", delimiters = [":", "\\|"])
data class AoCInput(
    // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
    //      0  1                1
    @InputField(0) val id: String,
    @InputField(1, delimiters = [" +"]) val winning: List<Int>,
    @InputField(2, delimiters = [" +"]) val numbers: List<Int>
)

data class ScratchCard(val id: Int, val winning: List<Int>, val numbers: List<Int>,
    var processed: Boolean = false, var winningCount: Int = -1) {
    init {
        if (winningCount < 0)   // calculate the winning count only if it's not set in the constructor
            winningCount = (winning.toSet() intersect numbers.toSet()).size
    }
    fun points() = if (winningCount > 0) 2.toDouble().pow(winningCount - 1).toInt() else 0
    fun clone() = ScratchCard(this.id, this.winning, this.numbers, false, this.winningCount)
}