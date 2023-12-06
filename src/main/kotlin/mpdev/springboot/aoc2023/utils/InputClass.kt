package mpdev.springboot.aoc2023.utils

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class InputClass(val prefix: String = "", val delimiters: Array<String>, val suffix: String = "")