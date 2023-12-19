package mpdev.springboot.aoc2023.solutions.day19

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.*
import kotlin.math.max
import kotlin.math.min

@Serializable
@AocInClass(delimiters = [","], removePatterns = ["\\{", "\\}", "x=", "m=", "a=", "s="])
data class AoCInput(
    // {x=787,m=2655,a=1222,s=2876}
    //    0   1      2        3
    @AocInField(0) val x: Int,
    @AocInField(1) val m: Int,
    @AocInField(2) val a: Int,
    @AocInField(3) val s: Int
)

class MachineParts(input: List<String>) {

    private val aocInputList: List<AoCInput>
    val workflows: Map<String,Workflow>
    val partsList: List<MPart>
    private val startWf = "in"

    init {
        val (input1, input2) = splitInput(input)
        workflows = processInput1(input1)
        aocInputList = InputUtils(AoCInput::class.java).readAoCInput(input2)
        partsList = aocInputList.map { MPart(it.x, it.m, it.a, it.s) }
    }

    fun runWorkFlows(part: MPart): WFRes {
        var wfId = startWf
        while (true) {
            val wf = workflows[wfId]!!
            var ruleRes: Any = wf.defRes
            for (rule in wf.rules) {
                if (rule.test(part.get(rule.param1))) {
                    ruleRes = rule.result
                    break
                }
            }
            if (ruleRes is WFRes)
                return ruleRes
            wfId = ruleRes as String
        }
    }

    fun sumOfAcceptedAttributes() = "xmas".toCharArray().map { partsList
        .filter { part -> runWorkFlows(part) == WFRes.A } .map { part -> part.get(it) }
    }.flatten().sum()

    private val startRange = mutableMapOf('x' to 1 .. 4000, 'm' to 1 .. 4000, 'a' to 1 .. 4000, 's' to 1 .. 4000)
    val acceptedRanges = mutableListOf<Map<Char,IntRange>>()

    fun rangeCountCombis(range: Map<Char,IntRange>) =
        range.values.fold(1L) { acc, r -> acc * (r.last - r.first + 1)  }

    // TODO simplify below function
    fun processRanges(range: Map<Char,IntRange> = startRange, wfId: String = startWf) {
        var thisRange = range.toMap()
        val wf = workflows[wfId]!!
        val ruleRes: Any = wf.defRes
        for (rule in wf.rules) {
            val (rangeT, rangeF) = findRangeTFforRule(range, rule)
            if (rule.result == WFRes.A)
                acceptedRanges.add(rangeT)
            else if (rule.result is String)
                processRanges(rangeT, rule.result)
            if (rangeF[rule.param1]?.first!! < 0)
                break
            thisRange = rangeF
        }
        if (ruleRes == WFRes.A)
            acceptedRanges.add(thisRange)
        else if (ruleRes is String)
            processRanges(thisRange, ruleRes)
    }

    fun findRangeTFforRule(range: Map<Char,IntRange>, rule: WFRule):
            Pair<Map<Char,IntRange>, Map<Char,IntRange>> {
        var rangeT = IntRange(-2, -1)
        var rangeF = IntRange(-2, -1)
        val r = range[rule.param1]!!
        if (rule.compare == Comparison.GT) {
            if (rule.param2 < r.last) {
                rangeT = IntRange(max(r.first, rule.param2 + 1), r.last)
                if (rule.param2 > r.first)
                    rangeF = IntRange(r.first, rule.param2)
            }
        }
        else {
            if (rule.param2 > r.first) {
                rangeT = IntRange(r.first, min(rule.param2 - 1, r.last))
                if (rule.param2 < r.last)
                    rangeF = IntRange(rule.param2, r.last)
            }
        }
        return Pair(range.toMutableMap().also { it[rule.param1] = rangeT },
            range.toMutableMap().also { it[rule.param1] = rangeF })
    }

    private fun splitInput(input: List<String>): Pair<List<String>, List<String>> {
        val input1 = mutableListOf<String>()
        val input2 = mutableListOf<String>()
        var state = 0
        input.forEach { line ->
            if (line.isEmpty()) { ++state; return@forEach }
            if (state == 0) input1.add(line)
            else input2.add(line)
        }
        return Pair(input1, input2)
    }

    private fun processInput1(input1: List<String>): Map<String,Workflow> {
        val wList = mutableMapOf<String,Workflow>()
        input1.forEach { line ->
            val fields = line.removeSuffix("}").split("{")
            val rules = fields[1].split(",").toMutableList().also { it.removeLast() }.map { WFRule.of(it) }
            val defRes = fields[1].split(",").last()
            wList[fields[0]] = Workflow(fields[0], rules, if (defRes == "A" || defRes == "R") WFRes.valueOf(defRes) else defRes)
        }
        return wList
    }
}

data class MPart(val x: Int, val m: Int, val a: Int, val s: Int) {
    fun get(f: Char) = when (f) {
        'x' -> x; 'm' -> m; 'a' -> a; 's' -> s
        else -> -1
    }
}

data class Workflow(val id: String, val rules: List<WFRule>, val defRes: Any)

data class WFRule(val param1: Char, val param2: Int, val compare: Comparison, val result: Any) {
    fun test(value: Int) =
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
                    if (res == "A" || res == "R") WFRes.valueOf(res) else res
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
enum class WFRes {
    A, R, W
}