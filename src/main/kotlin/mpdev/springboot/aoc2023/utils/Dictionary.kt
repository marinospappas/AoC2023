package mpdev.springboot.aoc2023.utils

class Dictionary() {

    private val FIRST_MAPPED_VALUE = 10000
    private var dictMapper = mutableMapOf<String,Int>()
    private var mappedValue = FIRST_MAPPED_VALUE

    operator fun get(key: String) =
        dictMapper.getOrPut(key) { ++mappedValue }

    fun keyFromMappedValue(mappedValue: Int) =
        dictMapper.entries.filter { it.value == mappedValue }.map { it.key }.first()

    fun list() = dictMapper.entries

    fun keys() = dictMapper.keys

    fun mappedValues() = dictMapper.values
}