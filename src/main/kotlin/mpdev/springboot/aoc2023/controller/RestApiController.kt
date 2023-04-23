package mpdev.springboot.aoc2023.controller

import mpdev.springboot.aoc2023.input.InputDataReader
import mpdev.springboot.aoc2023.model.PuzzleSolution
import mpdev.springboot.aoc2023.solutions.PuzzleSolver
import mpdev.springboot.aoc2023.utils.AoCUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/aoc2023")
class RestApiController(@Autowired var puzzleSolvers: List<PuzzleSolver>, @Autowired var inputDataReader: InputDataReader) {

    @GetMapping("/day/{day}", produces = ["application/json"])
    fun getAllEvents(@PathVariable day: Int): PuzzleSolution {
        val puzzleSolver = puzzleSolvers[AoCUtils.getIndexOfDay(day)].also { it.inputData = inputDataReader.read(day) }
        return puzzleSolver.solve()
    }

}