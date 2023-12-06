package mpdev.springboot.aoc2023.utils

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class InputField(val fieldId: Int, val delimiter: String = ", ")     // delimiter valid only for List