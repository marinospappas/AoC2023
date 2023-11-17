package mpdev.springboot.aoc2023.model

import com.fasterxml.jackson.annotation.JsonProperty

data class PuzzleSolution(
    @JsonProperty("Message")  var message: String = "Advent of Code 2023",
    @JsonProperty("Day")  var day: Int,
    @JsonProperty("initialisation Time")  var initTime: Long = 0,
    @JsonProperty("Time Unit") var initTimeUnit: String = "milli-sec",
    @JsonProperty("Solution")  var solution: Set<PuzzlePartSolution>
)

data class PuzzlePartSolution(
    @JsonProperty("Part") var part: Int = 0,
    @JsonProperty("Result") var result: String = "",
    @JsonProperty("Elapsed Time") var elapsedTime: Long = 0,
    @JsonProperty("Time Unit") var timeUnit: String = "milli-sec"
)
