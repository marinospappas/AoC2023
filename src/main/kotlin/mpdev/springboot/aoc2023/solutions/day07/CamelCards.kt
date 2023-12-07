package mpdev.springboot.aoc2023.solutions.day07

import mpdev.springboot.aoc2023.solutions.day07.CamelCards.Companion.JOKER

class CamelCards(input: List<String>) {

    val handsList = input.map {
        val arr = it.split(Regex(""" |\\t"""))
        Hand(arr[0], Integer.parseInt(arr[1]))
    }

    fun winnings(joker: Boolean = false): Long {
        val handsSorted = handsList.sortedWith(HandComparator(joker))
        return handsSorted.indices.sumOf { (it+1) * handsSorted[it].bid.toLong() }
    }

    companion object {
        const val JOKER = "J"
        private val cardStrengthList = listOf("A", "K", "Q", JOKER, "T", "9", "8", "7", "6", "5", "4", "3", "2").reversed()
        val cardStrengthListJoker = listOf("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2", JOKER).reversed()
        private val cardStrength = (cardStrengthList.indices).associateBy { cardStrengthList[it] }
        private val cardStrengthJoker = (cardStrengthListJoker.indices).associateBy { cardStrengthListJoker[it] }

        fun cardStrengthCompare(c1: String, c2: String, joker: Boolean = false): Int {
            val strength = if (joker) cardStrengthJoker else cardStrength
            for (i in c1.indices) {
                if (strength[c1.substring(i,i+1)]!! > strength[c2.substring(i,i+1)]!!)
                    return 1
                if (strength[c1.substring(i,i+1)]!! < strength[c2.substring(i,i+1)]!!)
                    return -1
            }
            return 0
        }
    }
}

data class Hand(val cards: String, val bid: Int = 0)

class HandComparator(var joker: Boolean = false): Comparator<Hand> {
    override fun compare(h1: Hand, h2: Hand): Int {
        if (HandType.getType(h1, joker) > HandType.getType(h2, joker))
            return 1
        if (HandType.getType(h1, joker) < HandType.getType(h2, joker))
            return -1
        return CamelCards.cardStrengthCompare(h1.cards, h2.cards, joker)
    }
}

enum class HandType(val test: (List<List<Char>>) -> Boolean) {
    HC({_ -> true}),
    OneP({ cards -> cards.maxOf { it.size } == 2  && cards.size == 4 }),
    TwoP({ cards -> cards.maxOf { it.size } == 2 && cards.size == 3 }),
    Three({ cards -> cards.maxOf { it.size } == 3 && cards.size == 3 }),
    Full({ cards -> cards.maxOf { it.size } == 3 && cards.size == 2 }),
    Four({ cards -> cards.maxOf { it.size } == 4 }),
    Five({ cards -> cards.size == 1 });

    companion object {
        fun getType(hand: Hand, joker: Boolean = false) =
            values().reversed().first { type -> checkCondition(hand, type, joker) }

        private fun checkCondition(hand: Hand, type: HandType, joker: Boolean = false): Boolean {
            if (joker && checkJoker(hand, type))
                return true
            return type.test(hand.cards.toCharArray().groupBy { it }.values.toList())
        }

        private fun checkJoker(hand: Hand, type: HandType): Boolean {
            if (!hand.cards.contains(JOKER))
                return checkCondition(hand, type)
            for (i in 1 until CamelCards.cardStrengthListJoker.size) {
                val c = CamelCards.cardStrengthListJoker[i]
                val test = Hand(hand.cards.replace(Regex(JOKER), c))
                if (checkCondition(test, type))
                    return true
            }
            return checkCondition(hand, type)
        }
    }
}
