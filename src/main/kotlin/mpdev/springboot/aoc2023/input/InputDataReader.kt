package mpdev.springboot.aoc2023.input

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File

@Component
class InputDataReader(@Value("\${input.filename.pattern}") var inputFileNamePattern: String) {

    fun read(day: Int): List<String>  =
        File("$inputFileNamePattern${String.format("%02d",day)}.txt").readLines()

}