package mpdev.springboot.aoc2023.utils

import mpdev.springboot.aoc2023.utils.ListType.*

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class InputClass(val prefix: String = "", val delimiters: Array<String>, val suffix: String = "")

// this type is used to define the type of collections
enum class ListType { string, int, long, pair }

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class InputField(val fieldId: Int, val delimiter: String = " *, +", val listType: ListType = string)     // delimiter amd type valid only for List

class InputUtils {
    companion object {
        const val FIELD_SEPARATOR = "££££"
        private var removePrefix = ""
        private var delimiters: Array<String> = arrayOf()
        private var removeSuffix = ""

        // transform an input line (removes noise)
        // and convert to json string ready for deserialization to a clazz object
        fun toJson(s: String, clazz: Class<*>): String {
            setDelimitersFromClass(clazz)
            val sArray = transform(s).split(FIELD_SEPARATOR)
            val mappings = getFieldMappings(clazz)
            return mappings.indices.joinToString(", ", "{ ", " }") { i ->
                fieldToJson(mappings[i].name, sArray[i], mappings[i].type, mappings[i].annotation)
            }
        }

        // remove noise from input string and convert it to list of values
        private fun transform(s: String): String {
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

        // set the prefix, field delimiters and suffix from the input class annotation
        private fun setDelimitersFromClass(clazz: Class<*>) {
            if (clazz.isAnnotationPresent(InputClass::class.java)) {
                val delims = clazz.getAnnotation(InputClass::class.java).delimiters
                delimiters = Array(delims.size) { delims[it] }
                removePrefix = clazz.getAnnotation(InputClass::class.java).prefix
                removeSuffix = clazz.getAnnotation(InputClass::class.java).suffix
            }
        }

        // convert  field to json format "name": value
        private fun fieldToJson(name: String, value: String, type: Class<*>, annotation: InputField): String {
            var result = ""
            result += """"$name": """
            result += getFieldValueForJson(value, type, annotation)
            return result
        }

        // get the value of a field as json-frienldy string
        private fun getFieldValueForJson(value: String, type: Class<*>, annotation: InputField): String {
            return when (type) {
                String::class.java -> """"${toString(value)}""""
                Int::class.java -> """"${toInt(value)}""""
                Long::class.java -> """"${toLong(value)}""""
                List::class.java -> toList(value, annotation)
                Pair::class.java -> toPair(value, annotation)
                else -> throw AocException("could not recognize field type [${type.simpleName}]")
            }
        }

        // update the fields map from the fields annotations
        private fun getFieldMappings(clazz: Class<*>): List<FieldMapping> {
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
        private fun toList(s: String, annotation: InputField): String {
            return s.trim().split(Regex(annotation.delimiter))
                .joinToString(", ", "[", "]") { """"${
                    when (annotation.listType) {
                        string, int, long -> it
                        pair -> toPair(it,annotation)
                    }
                }"""" }

            //val listDelim = annotation.delimiter
            //return when (annotation.listType) {
             //   string, int, long -> s.trim().split(Regex(listDelim)).joinToString(", ", "[", "]") { """"$it"""" }
               // pair -> s.trim().split(Regex(listDelim)).joinToString(", ", "[", "]") { """"${toPair(it,annotation)}"""" }
            //}
        }
        private fun toPair(s: String, annotation: InputField): String {
            val listDelim = annotation.delimiter
            val strArr = s.trim().split(Regex(" "))
            return  """{ "first": "${strArr[0].trim()}", "second": "${strArr[1].trim()}" }"""
        }


        // map of field name, index in input stream and type as per annotation in input data class
        data class FieldMapping(val name: String, val indx: Int, val type: Class<*>, val annotation: InputField)
    }

}

fun String.toJson(clazz: Class<*>) = InputUtils.toJson(this, clazz)