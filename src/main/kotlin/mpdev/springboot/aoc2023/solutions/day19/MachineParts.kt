package mpdev.springboot.aoc2023.solutions.day19

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.*
import kotlin.math.max
import kotlin.math.min

@Serializable
@AocInClass(delimiters = ["\\{"])
@AocInRemovePatterns(["\\}"])
data class AoCInput1(
    // rfg{s<537:gd,x>2440:R,A}
    // 0   1
    @AocInField(0) val wfId: String,
    @AocInField(1, delimiters = [","]) val rules: List<String>
)
@Serializable
@AocInClass(delimiters = [","])
@AocInRemovePatterns(["\\{", "\\}", "x=", "m=", "a=", "s="])
data class AoCInput2(
    // {x=787,m=2655,a=1222,s=2876}
    //    0   1      2        3
    @AocInField(0) val x: Int,
    @AocInField(1) val m: Int,
    @AocInField(2) val a: Int,
    @AocInField(3) val s: Int
)

class MachineParts(input: List<String>) {

    private val aocInputList1: List<AoCInput1>
    private val aocInputList2: List<AoCInput2>
    val workflows: Map<String,Workflow>
    val partsList: List<MPart>
    private val startWf = "in"

    init {
        val (input1, input2) = input.joinToString("|").split("||")
        aocInputList1 = InputUtils(AoCInput1::class.java).readAoCInput(input1.split("|"))
        workflows = aocInputList1.map { rec ->
            val rules = rec.rules.toMutableList().also { it.removeLast() }.map { WFRule.of(it) }
            val defRes = rec.rules.last()
            Workflow(rec.wfId, rules, if (defRes == "A" || defRes == "R") RuleResult.valueOf(defRes) else defRes)
        }.associateBy { wf -> wf.id }
        aocInputList2 = InputUtils(AoCInput2::class.java).readAoCInput(input2.split("|"))
        partsList = aocInputList2.map { MPart(it.x, it.m, it.a, it.s) }
    }

    fun runWorkFlows(part: MPart): RuleResult {
        var wfId = startWf
        while (true) {
            val wf = workflows[wfId]!!
            var ruleRes: Any = wf.defRes
            for (rule in wf.rules) {
                if (rule.test(part[rule.param1])) {
                    ruleRes = rule.result
                    break
                }
            }
            if (ruleRes is RuleResult)
                return ruleRes
            wfId = ruleRes as String
        }
    }

    fun sumOfAcceptedAttributes() = "xmas".toCharArray().map { partsList
        .filter { part -> runWorkFlows(part) == RuleResult.A }
        .map { part -> part[it] }
    }.flatten().sum()

    private val startRanges = mutableMapOf('x' to 1 .. 4000, 'm' to 1 .. 4000, 'a' to 1 .. 4000, 's' to 1 .. 4000)
    val acceptedRanges = mutableListOf<Map<Char,IntRange>>()

    /**
     * calculates the total number of individual values combinations from the map of 4 ranges (for the xmas attributes)
     */
    fun countCombinationsFromRanges(ranges: Map<Char,IntRange>) =
        ranges.values.fold(1L) { acc, rng -> acc * (rng.last - rng.first + 1)  }

    /**
     * identifies the range for each of the "xmas" attributes that result in "Accepted"
     * runs each rule of each workflow in the chain starting with the start ranges 1..4000 for all 4
     * it then restricts the input ranges to those that result in A
     * by calculating the 2 sub-ranges that succeed and fail each rule
     */
    fun identifyAcceptedRanges(ranges: Map<Char,IntRange> = startRanges, wfId: String = startWf) {
        var thisRanges = ranges.toMap()
        val wf = workflows[wfId]!!
        val ruleRes: Any = wf.defRes
        for (rule in wf.rules) {
            val (rangesTrue, rangesFalse) = findRangeTFforRule(thisRanges, rule)
            if (rule.result == RuleResult.A)
                acceptedRanges.add(rangesTrue)
            else if (rule.result is String)
                identifyAcceptedRanges(rangesTrue, rule.result)
            if (rangesFalse[rule.param1]?.first!! < 0)  // if the rule always succeeds stop the loop
                break
            thisRanges = rangesFalse.toMap()   // else continue into the next rule using the ranges that failed this rule
        }
        if (ruleRes == RuleResult.A)
            acceptedRanges.add(thisRanges)  // accepted
        else if (ruleRes is String)
            identifyAcceptedRanges(thisRanges, ruleRes)  // send to next workflow
    }

    // identify the two ranges for each attribute that make a rule succeed or fail
    private fun findRangeTFforRule(ranges: Map<Char,IntRange>, rule: WFRule): Pair<Map<Char,IntRange>, Map<Char,IntRange>> {
        var rangeT = IntRange(-2, -1)
        var rangeF = IntRange(-2, -1)
        val range = ranges[rule.param1]!!
        if (rule.compare == Comparison.GT) {
            if (rule.param2 < range.last) {
                rangeT = IntRange(max(range.first, rule.param2 + 1), range.last)
                if (rule.param2 > range.first)
                    rangeF = IntRange(range.first, rule.param2)
            }
        }
        else {
            if (rule.param2 > range.first) {
                rangeT = IntRange(range.first, min(rule.param2 - 1, range.last))
                if (rule.param2 < range.last)
                    rangeF = IntRange(rule.param2, range.last)
            }
        }
        return Pair(ranges.toMutableMap().also { it[rule.param1] = rangeT },
            ranges.toMutableMap().also { it[rule.param1] = rangeF })
    }
}

data class MPart(val x: Int, val m: Int, val a: Int, val s: Int) {
    operator fun get(f: Char) = when (f) {
        'x' -> x; 'm' -> m; 'a' -> a; 's' -> s
        else -> -1
    }
}

data class Workflow(val id: String, val rules: List<WFRule>, val defRes: Any)

data class WFRule(val param1: Char, val param2: Int, val compare: Comparison, val result: Any) {
    fun test(value: Int): Boolean =
        if (compare == Comparison.GT)
            value > param2
        else
            value < param2

    companion object {
        fun of(s: String): WFRule {
            val match = Regex("""([a-z])([<>])(\d+):([a-zAR]+)""").find(s)
            try {
                val (param1, comp, param2, res) = match!!.destructured
                return WFRule(param1.first(), param2.toInt(),
                    if (comp == "<") Comparison.LT else Comparison.GT,
                    if (res == "A" || res == "R") RuleResult.valueOf(res) else res
                )
            } catch (e: Exception) {
                throw AocException("invalid input $s")
            }
        }
    }
}

enum class Comparison {
    GT, LT
}
enum class RuleResult {
    A, R, W     // accept, reject, or send to further workflow
}