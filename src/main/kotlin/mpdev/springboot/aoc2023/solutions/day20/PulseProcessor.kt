package mpdev.springboot.aoc2023.solutions.day20

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.*
import kotlin.collections.ArrayDeque

@Serializable
@AocInClass(delimiters = ["->"])
data class AoCInput(
    // broadcaster -> a
    // %a -> inv, con
    // 0     1    2
    @AocInField(0) val sender: String,
    @AocInField(1, delimiters = [","]) val receivers: List<String>
)

class PulseProcessor(input: List<String>) {

    var debug = false
    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    val modules: Map<String,Module> = aocInputList.associate {
        val id = if (it.sender.startsWith('b')) it.sender else it.sender.substring(1, it.sender.length)
        id to Module(id, ModuleType.of(it.sender.first()), it.receivers)
    }
    private val startPulse = Pulse(PulseType.LOW, "button")
    private val startModule = "broadcaster"
    private val endModule = (modules.values.map { it.receivers }.flatten().distinct().toSet()
            - modules.keys).first()
    init {
        updateConjState()
    }

    fun processPulse(): Pair<Int,Int> {
        val count = mutableMapOf(PulseType.LOW to 0, PulseType.HIGH to 0, PulseType.NA to 0)
        count[startPulse.hl] =  count[startPulse.hl]?.plus(1)!!
        val queue = ArrayDeque<Pair<Module,Pulse>>().also { q -> q.add(Pair(modules[startModule]!!, startPulse)) }
        while (queue.isNotEmpty()) {
            val (curModule, input) = queue.removeFirst()
            curModule.outPulse = curModule.output(input)
            if (debug) println("module ${curModule.id} received ${input.hl} from ${input.sender}, new state ${curModule.state}")
            for (rcvr in curModule.receivers) {
                count[curModule.outPulse] = count[curModule.outPulse]?.plus(1)!!
                if (rcvr == endModule) {
                    if (debug) println("module $endModule received ${curModule.outPulse} from ${curModule.id}")
                    continue
                }
                if (curModule.outPulse != PulseType.NA)
                    queue.add(Pair(modules[rcvr]!!, Pulse(curModule.outPulse, curModule.id)))
            }
        }
        return Pair(count[PulseType.LOW]!!, count[PulseType.HIGH]!!)
    }

    fun countPulsesPart1(): Int {
        var countL = 0
        var countH = 0
        repeat(1000) {
            val (l, h) = processPulse()
            countL += l
            countH += h
        }
        return countL * countH
    }

    private fun updateConjState() {
        modules.values.filter { it.type == ModuleType.C }.forEach { conj ->
            modules.values.forEach { mod ->
                if (mod.receivers.contains(conj.id))
                    conj.receivedFrom[mod.id] = PulseType.LOW
            }
        }
    }
}

data class Module(val id: String, val type: ModuleType, val receivers: List<String>,
                  var state: ModuleState = ModuleState.LOW, val receivedFrom: MutableMap<String,PulseType> = mutableMapOf()
) {
    var outPulse = PulseType.NA
    fun output(input: Pulse): PulseType {
        return when(type) {
            ModuleType.F -> {
                if (input.hl == PulseType.LOW) {
                    state = state.invert()
                    PulseType.of(state)
                }
                else
                    PulseType.NA
            }
            ModuleType.C -> {
                receivedFrom[input.sender] = input.hl
                if (receivedFrom.values.all { it == PulseType.HIGH }) PulseType.LOW
                else PulseType.HIGH
            }
            ModuleType.B -> input.hl
        }
    }
}

data class Pulse(val hl:PulseType, val sender: String)

enum class ModuleState(val value: UInt) {
    HIGH(1u),
    LOW(0u);
    fun invert(): ModuleState = if (this == HIGH) LOW else HIGH
}

enum class PulseType(val value: UInt) {
    HIGH(1u),
    LOW(0u),
    NA(UInt.MAX_VALUE);
    companion object {
        fun of(state: ModuleState) = if (state == ModuleState.HIGH) HIGH else LOW
    }
}

enum class ModuleType(val value: Char) {
    F('%'),
    C('&'),
    B('b');
    companion object {
        fun of(c: Char): ModuleType = ModuleType.values().firstOrNull { it.value == c }
            ?: throw AocException("invalid module type [$c]")
    }
}