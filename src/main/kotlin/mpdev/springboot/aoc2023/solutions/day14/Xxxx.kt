package mpdev.springboot.aoc2023.solutions.day14

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2023.utils.AocInClass
import mpdev.springboot.aoc2023.utils.AocInField
import mpdev.springboot.aoc2023.utils.InputUtils

@Serializable
@AocInClass(delimiters = [" "])
data class AoCInput(
    // ???.### 1,1,3
    // 0       1
    @AocInField(0) val s: String,
    @AocInField(1, delimiters = [","]) val numList: List<Int>
)

class Xxxx(input: List<String>) {

    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    //val records: List<Pair<String,List<Int>>> = aocInputList.map { Pair(it.s.replace(Regex("""\."""),"1").replace(Regex("""#"""),"0"), it.numList) }


}
