package mpdev.springboot.aoc2023.solutions.day07

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.solutions.day07.CamelCards.Companion.JOKER
import mpdev.springboot.aoc2023.utils.AocInClass
import mpdev.springboot.aoc2023.utils.AocInField
import mpdev.springboot.aoc2023.utils.InputUtils

class CamelCards(input: List<String>) {

    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    val handsList: List<Hand> = aocInputList.map { Hand(it.cards, it.bid) }

    fun winnings(joker: Boolean = false): Long {
        val handsSorted = handsList.sortedWith(HandComparator(joker))
        return handsSorted.indices.sumOf { (it+1) * handsSorted[it].bid.toLong() }
    }

    companion object {
        const val JOKER = 'J'
        private val cardStrengthList = listOf('A', 'K', 'Q', JOKER, 'T', '9', '8', '7', '6', '5', '4', '3', '2').reversed()
        private val cardStrengthListJoker = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', JOKER).reversed()
        private val cardStrength = (cardStrengthList.indices).associateBy { cardStrengthList[it] }
        private val cardStrengthJoker = (cardStrengthListJoker.indices).associateBy { cardStrengthListJoker[it] }

        fun cardStrengthCompare(c1: String, c2: String, joker: Boolean = false): Int {
            val strength = if (joker) cardStrengthJoker else cardStrength
            for (i in c1.indices) {
                if (strength[c1[i]]!! > strength[c2[i]]!!)
                    return 1
                if (strength[c1[i]]!! < strength[c2[i]]!!)
                    return -1
            }
            return 0
        }
    }
}

data class Hand(val cards: String, val bid: Int = 0)

class HandComparator(private var joker: Boolean = false): Comparator<Hand> {
    override fun compare(h1: Hand, h2: Hand): Int {
        val t1 = HandType.getType(h1, joker)
        val t2 = HandType.getType(h2, joker)
        if (t1 > t2) return 1
        if (t1 < t2) return -1
        return CamelCards.cardStrengthCompare(h1.cards, h2.cards, joker)
    }
}

enum class HandType(val test: (List<List<Char>>) -> Boolean) {
    HighCard({ _ -> true }),
    OnePair({ cards -> cards.maxOf { it.size } == 2  && cards.size == 4 }),
    TwoPairs({ cards -> cards.maxOf { it.size } == 2 && cards.size == 3 }),
    ThreeOfaKind({ cards -> cards.maxOf { it.size } == 3 && cards.size == 3 }),
    FullHouse({ cards -> cards.maxOf { it.size } == 3 && cards.size == 2 }),
    FourOfaKind({ cards -> cards.maxOf { it.size } == 4 }),
    FiveOfaKind({ cards -> cards.size == 1 });

    companion object {
        fun getType(hand: Hand, joker: Boolean = false): HandType {
            // group all cards to list of lists by
            val allCardsGrouped = hand.cards.toCharArray().groupBy { it }.values.toList().sortedBy { it.size }.reversed()
            if (joker && hand.cards.contains(JOKER)) {
                // remove the joker from the grouped cards
                val cardsGrouped = allCardsGrouped.filterNot { it.first() == JOKER } // hand.cards.replace(Regex(JOKER.toString()), "").toCharArray().groupBy { it }.values.toList().sortedBy { it.size }.reversed()
                val groupSizes = cardsGrouped.map { it.size }.toSet()
                return when (hand.cards.count { it == JOKER }) {
                    4, 5 -> FiveOfaKind
                    3 -> if (groupSizes == setOf(2)) FiveOfaKind else FourOfaKind
                    2 -> when (groupSizes) {
                        setOf(3) -> FiveOfaKind
                        setOf(2,1) -> FourOfaKind
                        else -> ThreeOfaKind
                    }
                    // 1 Joker
                    else -> when (groupSizes) {
                        setOf(4) -> FiveOfaKind
                        setOf(3,1) -> FourOfaKind
                        setOf(2,2) -> FullHouse
                        setOf(2,1,1) -> ThreeOfaKind
                        else -> OnePair
                    }
                }
            }
            return values().reversed().first { type -> type.test(allCardsGrouped) }
        }
    }
}

@Serializable
@AocInClass(delimiters = [" "])
data class AoCInput(
    // 32T3K 765
    // 0     1
    @AocInField(0) val cards: String,
    @AocInField(1) val bid: Int
)