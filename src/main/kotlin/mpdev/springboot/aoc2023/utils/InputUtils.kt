package mpdev.springboot.aoc2023.utils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mpdev.springboot.aoc2023.utils.ListType.*

/**
 * Annotation for the class that will receive the input
 * attributes:
 *      delimiters: a series of delimiters to separate the fields in each line
 *      skip lines: skip n lines from the beginning of the stream
 *      skip empty lines: if true, ignore empty lines
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class AocInClass(val delimiters: Array<String> = [" "], val removePatterns: Array<String> = [],
                            val skipLines: Int = 0, val skipEmptyLines: Boolean = true)

/**
 * List of patterns to be replaced with the patterns of the second list
 * the format is: pattern1, replace1, pattern2, replace2, ...
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class AocInReplacePatterns(val patterns: Array<String> = [])

/**
 * List of patterns to be removed
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class AocInRemovePatterns(val patterns: Array<String> = [])

/**
 * Strings that match any of the patterns will be retained and can be accessed in replacement patterns
 * as &1, &2, ... in order they are listed
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class AocInRetainValues(val patterns: Array<String> = [])


// this type is used to define the type of collections
enum class ListType { string, int, long, pair, list, point }

/**
 * Annotation for each input field
 * attributes:
 *      field id:
 *      delimiters: a series of delimiters to separate the fields in each line
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class AocInField(val fieldId: Int, val delimiters:  Array<String> = [","],
                            val listType: Array<ListType> = [string])
                            // list type and delimiter 0 valid only for List, delimiters 1 and above valid only for list of collection

class InputUtils(inputClazz: Class<*>) {

    companion object {
        const val FIELD_SEPARATOR = "££££"
        private var retainedValues: Array<String> = emptyArray()
        var skipEmptyLines: Boolean = true
        var skipLines: Int = 0
    }
    private var clazz: Class<*> = inputClazz
    private var delimiters: Array<String> = arrayOf()
    private var replacePatterns: List<Pair<String, String>> = listOf()
    private var removePatterns: List<String> = listOf()
    private var retainPatterns: List<String> = listOf()
    private var mappings: List<FieldMapping> = listOf()

    init {
        // set the prefix, field delimiters and suffix from the input class annotation and other parameters
        if (clazz.isAnnotationPresent(AocInClass::class.java)) {
            val delims = clazz.getAnnotation(AocInClass::class.java).delimiters
            delimiters = Array(delims.size) { delims[it] }
            skipLines = clazz.getAnnotation(AocInClass::class.java).skipLines
            removePatterns = clazz.getAnnotation(AocInClass::class.java).removePatterns.toList()
            skipEmptyLines = clazz.getAnnotation(AocInClass::class.java).skipEmptyLines
            mappings = getFieldMappings()
            if (mappings.size == 1)
               delimiters = arrayOf("NA-NA-NA-NA-NA-NA-NA")
        }
        else
            throw AocException("AoCInput class must be annotated @InputClass")
        if (clazz.isAnnotationPresent(AocInRemovePatterns::class.java)) {
            removePatterns = clazz.getAnnotation(AocInRemovePatterns::class.java).patterns.toList()
        }
        if (clazz.isAnnotationPresent(AocInReplacePatterns::class.java)) {
            val replaceList = mutableListOf<Pair<String,String>>()
            for (i in 0 until clazz.getAnnotation(AocInReplacePatterns::class.java).patterns.lastIndex step(2))
                replaceList.add(Pair(
                    clazz.getAnnotation(AocInReplacePatterns::class.java).patterns[i],
                    clazz.getAnnotation(AocInReplacePatterns::class.java).patterns[i+1]
                ))
            replacePatterns = replaceList
        }
        if (clazz.isAnnotationPresent(AocInRetainValues::class.java)) {
            retainPatterns = clazz.getAnnotation(AocInRetainValues::class.java).patterns.toList()
            if (retainedValues.isEmpty())
                retainedValues = Array(retainPatterns.size){""}
        }
    }

    inline fun <reified T> readAoCInput(input: List<String>): List<T> {
        return Json.decodeFromString(
            input
                .stream().skip(skipLines.toLong()).toList()
                .map { transform(it) }
                .filterNot { skipEmptyLines && it.isEmpty() }
                .joinToString(",", "[", "]") { toJson(it) }
        )
    }

    // transform an input line (removes noise)
    // and convert to json string ready for deserialization to a clazz object
    fun toJson(s: String): String {
        val sArray = s.split(FIELD_SEPARATOR)
        return mappings.indices.joinToString(", ", "{ ", " }") { i ->
            fieldToJson(mappings[i].name, sArray[i], mappings[i].type, mappings[i].annotation)
        }
    }

    // remove noise from input string and convert it to list of values
    fun transform(s: String): String {
        var s1 = s
        for (pattern in removePatterns)
            s1 = s1.replace(Regex(pattern), "")

        for (i in replacePatterns.indices) {
            if (s1.isEmpty())
                break
            var replacement = replacePatterns[i].second
            if (replacement.contains(Regex("""\$\d"""))) {
                val match = Regex(""".*\$(\d).*""").find(replacement)
                try {
                    val (value) = match!!.destructured
                    replacement = replacement.replace("$$value", retainedValues[Integer.parseInt(value)-1])
                    s1 = s1.replace(Regex(replacePatterns[i].first), replacement)
                } catch (ignore: Exception) {}
            }
            else
                s1 = s1.replace(Regex(replacePatterns[i].first), replacement)
        }

        for (i in retainPatterns.indices) {
            val pattern = retainPatterns[i]
            val match = Regex(pattern).find(s1)
            try {
                val (value) = match!!.destructured
                retainedValues[i] = value
            } catch (ignore: Exception) {}
        }

        return if (delimiters.isEmpty())
            s1
        else {
            delimiters.forEach { d -> s1 = s1.replace(Regex(d), FIELD_SEPARATOR) }
            s1
        }
    }

    // convert  field to json format "name": value
    private fun fieldToJson(name: String, value: String, type: Class<*>, annotation: AocInField): String {
        var result = ""
        result += """"$name": """
        result += getFieldValueForJson(value, type, annotation)
        return result
    }

    // get the value of a field as json-friendly string
    private fun getFieldValueForJson(value: String, type: Class<*>, annotation: AocInField): String {
        return when (type) {
            String::class.java -> """"${toString(value)}""""
            Int::class.java -> """"${toInt(value)}""""
            Long::class.java -> """"${toLong(value)}""""
            List::class.java -> toList(value, annotation, 0)
            Pair::class.java -> toPair(value, annotation.delimiters[0])
            Point::class.java -> toPoint(value, annotation.delimiters[0])
            else -> throw AocException("could not recognize field type [${type.simpleName}]")
        }
    }

    // update the fields map from the fields annotations
    private fun getFieldMappings(): List<FieldMapping> {
        val fieldMapppings = mutableListOf<FieldMapping>()
        clazz.declaredFields.forEach { f ->
            if (f.isAnnotationPresent(AocInField::class.java)) {
                val annotation = f.getAnnotation(AocInField::class.java)
                fieldMapppings.add(FieldMapping(f.name, annotation.fieldId, f.type, annotation))
            }
        }
        return fieldMapppings
    }

    // get the Json output for the value of a field for various field types
    private fun toString(s: String): String {
        return s.trim()
    }
    private fun toInt(s: String): String {
        return Integer.parseInt(s.trim()).toString()
    }
    private fun toLong(s: String): String {
        return s.trim().toLong().toString()
    }
    private fun toList(s: String, annotation: AocInField, level: Int): String {
        return s.trim().split(Regex(annotation.delimiters[level]))
            .joinToString(", ", "[", "]") {
                when (annotation.listType[level]) {
                    string, int, long -> """"$it""""
                    pair -> toPair(it, annotation.delimiters[level+1])
                    point -> toPoint(it, annotation.delimiters[level+1])
                    list -> toList(it, annotation, level+1)
                }
            }
    }
    private fun toPair(s: String, delimiter: String): String {
        val strArr = s.trim().split(Regex(delimiter))
        return """{ "first": "${strArr[0].trim()}", "second": "${strArr[1].trim()}" }"""
    }
    private fun toPoint(s: String, delimiter: String): String {
        val strArr = s.trim().split(Regex(delimiter))
        return """{ "x": "${strArr[0].trim()}", "y": "${strArr[1].trim()}" }"""
    }

    // map of field name, index in input stream and type as per annotation in input data class
    data class FieldMapping(val name: String, val indx: Int, val type: Class<*>, val annotation: AocInField)

}
