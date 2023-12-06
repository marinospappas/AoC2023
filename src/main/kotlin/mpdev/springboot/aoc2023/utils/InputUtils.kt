package mpdev.springboot.aoc2023.utils

class InputUtils {
    companion object {
        private var clazz: Class<*>? = null
        const val FIELD_SEPARATOR = "££££"
        private var removePrefix = ""
        private var delimiters: Array<String> = arrayOf()
        private var removeSuffix = ""

        // convert a transformed input line to json string ready for deserialization
        fun toJson(s: String, clazz: Class<*>): String {
            val sArray = s.split(FIELD_SEPARATOR)
            val mappings = getFieldMappings(clazz)
            return mappings.indices.joinToString(", ", "{ ", " }") { i ->
                fieldToJson(mappings[i].name, sArray[i], mappings[i].type, mappings[i].annotation)
            }
        }

        // remove noise from input string and convert it to list of values
        fun transform(s: String, clazz: Class<*>): String {
            setDelimitersFromClass(clazz)
            var s1 = s
                .removePrefix(removePrefix)
                .removeSuffix(removeSuffix)
            return if (delimiters.isEmpty()) s1
            else {
                delimiters.forEach { d -> s1 = s1.replace(Regex(d), FIELD_SEPARATOR) }
                s1
            }
        }

        // set the prefix, field delimiters and suffix from the input class annotation
        private fun setDelimitersFromClass(clazz: Class<*>) {
            if (clazz.isAnnotationPresent(InputClass::class.java)) {
                Companion.clazz = clazz
                val delims = clazz.getAnnotation(InputClass::class.java).delimiters
                delimiters = Array(delims.size) { delims[it] }
                removePrefix = clazz.getAnnotation(InputClass::class.java).prefix
                removeSuffix = clazz.getAnnotation(InputClass::class.java).suffix
            }
        }

        // convert  field to json format "name": value
        private fun fieldToJson(name: String, value: String, type: Class<*>, annotation: Annotation): String {
            var result = ""
            result += """"$name": """
            result += getFieldValueForJson(value, type, annotation)
            return result
        }

        // get the value of a field as json-frienldy string
        private fun getFieldValueForJson(value: String, type: Class<*>, annotation: Annotation): String {
            return when (type) {
                String::class.java -> """"${toString(value)}""""
                Int::class.java -> """"${toInt(value)}""""
                Long::class.java -> """"${toLong(value)}""""
                List::class.java -> toList(value, annotation)
                else -> throw AocException("could not recognize field type [${type.simpleName}]")
            }
        }

        // update the fields map from the fields annotations
        private fun getFieldMappings(clazz: Class<*>): List<FieldMapping> {
            val fieldMapppings = mutableListOf<FieldMapping>()
            clazz.declaredFields.forEach { f ->
                if (f.isAnnotationPresent(InputField::class.java)) {
                    val annotation =  f.getAnnotation(InputField::class.java)
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
        private fun toList(s: String, annotation: Annotation): String {
            val listDelim = annotation
            return s.trim().split(Regex(" +")).map { """"$it"""" }.joinToString(", ", "[", "]")
        }

        // map of field name, index in input stream and type as per annotation in input data class
        data class FieldMapping(val name: String, val indx: Int, val type: Class<*>, val annotation: Annotation)
    }

}

fun String.transformInput(clazz: Class<*>) = InputUtils.transform(this, clazz)

fun String.toJson(clazz: Class<*>) = InputUtils.toJson(this.transformInput(clazz), clazz)