package mpdev.springboot.aoc2023.utils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mpdev.springboot.aoc2023.utils.ListType.*

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class InputClass(val prefix: String = "", val delimiters: Array<String> = [" "], val suffix: String = "")

// this type is used to define the type of collections
enum class ListType { string, int, long, pair, list, point }

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class InputField(val fieldId: Int, val delimiters:  Array<String> = [","],
                            val listType: Array<ListType> = [string])
                            // list type and delimiter 0 valid only for List, delimiters 1 and above valid only for list of collection

class InputUtils(inputClazz: Class<*>) {

    companion object {
        const val FIELD_SEPARATOR = "££££"
    }

    var clazz: Class<*> = inputClazz
    private var removePrefix = ""
    private var delimiters: Array<String> = arrayOf()
    private var removeSuffix = ""
    private var remove: Array<String> = arrayOf()
    private var replace: Array<Pair<String,String>> = arrayOf()

    init {
        // set the prefix, field delimiters and suffix from the input class annotation
        if (clazz.isAnnotationPresent(InputClass::class.java)) {
            val delims = clazz.getAnnotation(InputClass::class.java).delimiters
            delimiters = Array(delims.size) { delims[it] }
            removePrefix = clazz.getAnnotation(InputClass::class.java).prefix
            removeSuffix = clazz.getAnnotation(InputClass::class.java).suffix
        }
    }

    inline fun <reified T> readAoCInput(input: List<String>): List<T> {
        return Json.decodeFromString(
            input.joinToString(",", "[", "]") { toJson(it) }
        )
    }

    // transform an input line (removes noise)
    // and convert to json string ready for deserialization to a clazz object
    fun toJson(s: String): String {
        val sArray = transform(s).split(FIELD_SEPARATOR)
        val mappings = getFieldMappings()
        return mappings.indices.joinToString(", ", "{ ", " }") { i ->
            fieldToJson(mappings[i].name, sArray[i], mappings[i].type, mappings[i].annotation)
        }
    }

    // remove noise from input string and convert it to list of values
    fun transform(s: String): String {
        var s1 = s
            .removePrefix(removePrefix)
            .removeSuffix(removeSuffix)
        return if (delimiters.isEmpty())
            s1
        else {
            delimiters.forEach { d -> s1 = s1.replace(Regex(d), FIELD_SEPARATOR) }
            s1
        }
    }

    // convert  field to json format "name": value
    private fun fieldToJson(name: String, value: String, type: Class<*>, annotation: InputField): String {
        var result = ""
        result += """"$name": """
        result += getFieldValueForJson(value, type, annotation)
        return result
    }

    // get the value of a field as json-friendly string
    private fun getFieldValueForJson(value: String, type: Class<*>, annotation: InputField): String {
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
            if (f.isAnnotationPresent(InputField::class.java)) {
                val annotation = f.getAnnotation(InputField::class.java)
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
    private fun toList(s: String, annotation: InputField, level: Int): String {
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
    data class FieldMapping(val name: String, val indx: Int, val type: Class<*>, val annotation: InputField)

}
