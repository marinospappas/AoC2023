package mpdev.springboot.aoc2023.model

import com.fasterxml.jackson.annotation.JsonProperty

data class PuzzleSolution(
    @JsonProperty("Message")  var message: String = "Advent of Code 2023",
    @JsonProperty("Day")  var day: Int,
    @JsonProperty("Solution")  var solution: Set<PuzzlePartSolution>
)

data class PuzzlePartSolution(
    @JsonProperty("Part") var part: Int = 0,
    @JsonProperty("Result") var result: String = "",
    @JsonProperty("Elapsed Time (msec)") var elapsedTime: Long = 0
)
