import java.io.File
import java.lang.IllegalStateException
import java.math.BigInteger
import kotlin.math.ceil
import kotlin.math.roundToInt

class Day4 : Day<Int, Long> {

    companion object {
        private val MANDATORY_FIELDS = listOf(
            "byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"
        )

        private val PASSPORT_SEPARATOR = "\n(\\s)*\n".toRegex()
        private val FIELDS_SEPARATOR = "[ \n]".toRegex()
        private val FIELD_SEPARATOR = ":".toRegex()
        private val EYE_COLOURS = "amb blu brn gry grn hzl oth".split(" ")
        private val heigtCM = "([0-9]{3})(cm)$".toRegex()
        private val heigtIN = "([0-9]{2})(in)$".toRegex()
    }

    override val name: String = "Day 4"

    private val input = File("src/main/resources/day4.txt").readText()


    override fun answerQuestion1(): Int {
        val passports = input.toPassports()
        return passports.map { it.toPassportField() }
            .filter { fields -> MANDATORY_FIELDS.all { fields.containsKey(it) } }
            .count()
    }

    override fun answerQuestion2(): Long {
        return input.toPassports().map { it.toPassportField() }
            .filter { fields -> MANDATORY_FIELDS.all { fields.containsKey(it) } }
            .filter { it.entries.all { entry ->
                (entry.key to entry.value).isValid()
            } }
            .count().toLong()
    }

    private fun String.toPassports() = split(PASSPORT_SEPARATOR)
    private fun String.toPassportField() = split(FIELDS_SEPARATOR).map {
        val field = it.split(FIELD_SEPARATOR)
        field[0] to field[1]
    }.toMap()

    private fun Pair<String, String>.isValid(): Boolean = when (first) {
        "byr" -> (1920..2002).contains(second.toInt())
        "iyr" -> (2010..2020).contains(second.toInt())
        "eyr" -> (2020..2030).contains(second.toInt())
        "hgt" -> when {
            second.matches(heigtCM) -> (150..193).contains(heigtCM.matchEntire(second)!!.groupValues[1].toInt())
            second.matches(heigtIN) -> (59..76).contains(heigtIN.matchEntire(second)!!.groupValues[1].toInt())
            else -> false
        }
        "hcl" -> second.matches("#[0-9a-f]{6}".toRegex())
        "ecl" -> EYE_COLOURS.contains(second)
        "pid" -> second.matches("[0-9]{9}".toRegex())
        "cid" -> true
        else -> throw IllegalStateException("unknown field $first")
    }
}
