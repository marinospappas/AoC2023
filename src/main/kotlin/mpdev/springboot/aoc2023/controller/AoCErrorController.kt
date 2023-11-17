package mpdev.springboot.aoc2023.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AoCErrorController : ErrorController {

    protected val log: Logger = LoggerFactory.getLogger(this::class.java)

    @RequestMapping("/error", produces = ["application/json"])
    fun handleError(): String {
        log.error("invalid url called")
        return  """{ "Message" : "Error: page not implemented" }"""
    }
}
