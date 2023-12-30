package mpdev.springboot.aoc2023.solutions.day20

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.*

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

    companion object {
        const val broadcaster = "broadcaster"
        private val startPulse = Pulse(PulseType.LOW, "button", broadcaster)
    }
    var debug = false
    var debug2 = false
    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    val modules: Map<String,Module> = aocInputList.associate {
        val id = it.sender.substring(1, it.sender.length)
        when (it.sender.first()) {
            'b' -> broadcaster to Broadcaster(it.receivers)
            '%' -> id to FlipFlop(id, it.receivers)
            '&' -> id to Conjuction(id, it.receivers)
            else -> throw AocException("invalid input data [$it]")
        }
    }
    private val endModule = (modules.values.map { it.destinations }.flatten().distinct().toSet()
            - modules.keys).first()
    val endStateInputs = mutableListOf<Pair<Int,Pulse>>()
    var cycleCount = 0

    init {
        updateConjState()
    }

    fun processPulse(watchModuleId: String = ""): Pair<Int,Int> {
        val counts = mutableMapOf(PulseType.LOW to 0, PulseType.HIGH to 0, PulseType.NA to 0)
        counts[startPulse.hl] =  counts[startPulse.hl]?.plus(1)!!
        val queue = ArrayDeque<Pulse>().also { q -> q.add(startPulse) }
        while (queue.isNotEmpty()) {
            val inputPulse = queue.removeFirst()
            val curModuleId = inputPulse.destination
            if (curModuleId == watchModuleId)
                endStateInputs.add(Pair(cycleCount, inputPulse))
            val outputPulses = modules[curModuleId]?.sendPulses(inputPulse)!!
            if (debug) println("module $curModuleId received ${inputPulse.hl} from ${inputPulse.sender} - ${modules[curModuleId]}")
            for (pulse in outputPulses) {
                counts[pulse.hl] = counts[pulse.hl]?.plus(1)!!
                if (pulse.destination == endModule) {
                    continue
                }
                if (pulse.hl != PulseType.NA)
                    queue.add(pulse)
            }
        }
        return Pair(counts[PulseType.LOW]!!, counts[PulseType.HIGH]!!)
    }

    fun countPulsesPart1(): Int {
        var countL = 0
        var countH = 0
        repeat(1000) {
            processPulse().let { countL += it.first; countH += it.second }
        }
        return countL * countH
    }

    fun identifyighPulseCyclesForFinalConjuction(): Set<Long> {
        val moduleToWatch = modules.values.first { it.destinations.contains(endModule) }.id
        repeat(20000) {
            cycleCount = it + 1
            processPulse(moduleToWatch)
        }
        val inputCycles = endStateInputs.filter { it.second.hl == PulseType.HIGH }.groupBy { it.second.sender }
        if (debug2) inputCycles.forEach { it.println() }
        if (inputCycles.entries.size == (modules[moduleToWatch] as Conjuction).inputs.size &&
            inputCycles.values.all { it.size >= 3 && it[1].first == it[0].first * 2 && it[2].first == it[0].first * 3 } )
            return inputCycles.values.map { it[0].first.toLong() }.toSet()
        throw AocException("could not identify cycle of high signals")
    }

    private fun updateConjState() {
        modules.values.filterIsInstance<Conjuction>().forEach { conj: Conjuction ->
            modules.values.forEach { mod ->
                if (mod.destinations.contains(conj.id))
                    conj.inputs[mod.id] = PulseType.LOW
            }
        }
    }
}

class Broadcaster(receivers: List<String>): Module(PulseProcessor.broadcaster, receivers) {
    override fun output(inPulse: Pulse): PulseType = inPulse.hl
}

class FlipFlop(id: String, receivers: List<String>): Module(id, receivers) {
    private var state = ModuleState.LOW
    override fun output(inPulse: Pulse): PulseType =
        if (inPulse.hl == PulseType.LOW) {
            state = state.invert()
            PulseType.of(state)
        }
        else PulseType.NA
    override fun toString() = "F/F ${super.toString()} state: $state"
}

class Conjuction(id: String, receivers: List<String>): Module(id, receivers) {
    val inputs = mutableMapOf<String,PulseType>()
    override fun output(inPulse: Pulse): PulseType {
        inputs[inPulse.sender] = inPulse.hl
        return if (inputs.values.all { it == PulseType.HIGH }) PulseType.LOW
        else PulseType.HIGH
    }
    override fun toString() = "CON ${super.toString()} inputs: $inputs"
}

abstract class Module(val id: String, val destinations: List<String>) {
    abstract fun output(inPulse: Pulse): PulseType
    fun sendPulses(inPulse: Pulse): List<Pulse> {
        val outPulse = output(inPulse)
        return destinations.map { Pulse(outPulse, id, it) }
    }
    override fun toString(): String =
        "[$$id], xmits to: $destinations"
}

data class Pulse(val hl:PulseType, val sender: String, val destination: String)

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