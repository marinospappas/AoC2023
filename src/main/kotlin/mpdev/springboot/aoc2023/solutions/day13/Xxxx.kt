package mpdev.springboot.aoc2023.solutions.day13

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.InputClass
import mpdev.springboot.aoc2023.utils.InputField
import mpdev.springboot.aoc2023.utils.InputUtils
import kotlin.math.pow

@Serializable
@InputClass
data class AoCInput(
    // ???.### 1,1,3
    // 0       1
    @InputField(0) val s: String,
    @InputField(1, delimiters = [","]) val numList: List<Int>
)

class Xxxx(input: List<String>) {

    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    //val records = aocInputList.map { Pair(it.s.replace(Regex("""\."""),"1").replace(Regex("""#"""),"0"), it.numList) }

}