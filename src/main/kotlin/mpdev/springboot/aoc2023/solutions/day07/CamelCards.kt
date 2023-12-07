package mpdev.springboot.aoc2023.solutions.day07

class CamelCards(input: List<String>) {

    val handsList = input.map {
        val arr = it.split(Regex(""" |\\t"""))
        Hand(arr[0], arr[1].toLong().toInt())
    }

    fun winnings(joker: Boolean = false): Long {
        val handsSorted = handsList.sortedWith(HandComparator(joker))
        return handsSorted.indices.sumOf { (it+1) * handsSorted[it].bid.toLong() }
    }

    companion object {
        private val cardStrengthList = listOf("A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2").reversed()
        private val cardStrengthListJoker = listOf("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J").reversed()
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

        fun isFive(hand: Hand, joker: Boolean = false): Boolean {
            if (joker && checkJoker(hand, CamelCards::isFive))
                return true
            val cards = hand.cards.toCharArray()
            return cards.distinct().size == 1
        }
        fun isFour(hand: Hand, joker: Boolean = false): Boolean {
            if (joker && checkJoker(hand, CamelCards::isFour))
                return true
            val sortedCards = hand.cards.toCharArray().groupBy { it }.values
            return sortedCards.maxOf { it.size } == 4
        }
        fun isFull(hand: Hand, joker: Boolean = false): Boolean {
            if (joker && checkJoker(hand, CamelCards::isFull))
                return true
            val sortedCards = hand.cards.toCharArray().groupBy { it }.values
            return sortedCards.maxOf { it.size } == 3 && sortedCards.size == 2
        }
        fun isThree(hand: Hand, joker: Boolean = false): Boolean {
            if (joker && checkJoker(hand, CamelCards::isThree))
                return true
            val sortedCards = hand.cards.toCharArray().groupBy { it }.values
            return sortedCards.maxOf { it.size } == 3 && sortedCards.size == 3
        }
        fun isTwoPair(hand: Hand, joker: Boolean = false): Boolean {
            if (joker && checkJoker(hand, CamelCards::isTwoPair))
                return true
            val sortedCards = hand.cards.toCharArray().groupBy { it }.values
            return sortedCards.maxOf { it.size } == 2 && sortedCards.size == 3
        }
        fun isOnePair(hand: Hand, joker: Boolean = false): Boolean {
            if (joker && checkJoker(hand, CamelCards::isOnePair))
                return true
            val sortedCards = hand.cards.toCharArray().groupBy { it }.values
            return sortedCards.maxOf { it.size } == 2 && sortedCards.size == 4
        }
        private fun checkJoker(hand: Hand, testHand: (Hand) -> Boolean): Boolean {
            if (!hand.cards.contains("J"))
                return testHand(hand)
            for (i in 1 until cardStrengthListJoker.size) {
                val c = cardStrengthListJoker[i]
                val test = Hand(hand.cards.replace(Regex("J"), c))
                if (testHand(test))
                    return true
            }
            return testHand(hand)
        }
    }
}

data class Hand(val cards: String, val bid: Int = 0)

class HandComparator(var joker:Boolean = false): Comparator<Hand> {
    override fun compare(h1: Hand, h2: Hand): Int {
        if (HandType.getType(h1, joker) > HandType.getType(h2, joker))
            return 1
        if (HandType.getType(h1, joker) < HandType.getType(h2, joker))
            return -1
        return CamelCards.cardStrengthCompare(h1.cards, h2.cards, joker)
    }
}

enum class HandType(val test: (Hand, Boolean) -> Boolean) {
    HC({_,_ -> true}),
    OneP({h,j -> CamelCards.isOnePair(h,j)}),
    TwoP({h,j -> CamelCards.isTwoPair(h,j)}),
    Three({h,j -> CamelCards.isThree(h,j)}),
    Full({h,j -> CamelCards.isFull(h,j)}),
    Four({h,j -> CamelCards.isFour(h,j)}),
    Five({h,j -> CamelCards.isFive(h,j)});
    companion object {
        fun getType(hand: Hand, joker: Boolean = false) =
            values().reversed().first { it.test(hand, joker) }
    }
}
