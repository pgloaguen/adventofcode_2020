import java.io.File
import java.lang.IllegalStateException
import java.math.BigInteger
import java.util.*
import kotlin.math.ceil
import kotlin.math.roundToInt

class Day6 : Day<Int, Int> {

    companion object {
        private val GROUP_SEPARATOR = "\n(\\s)*\n".toRegex()
        private val ANSWERS_SEPARATOR = "[\n]".toRegex()
    }

    override val name: String = "Day 6"
    private val input = File("src/main/resources/day6.txt").readText()

    override fun answerQuestion1(): Int {
        return input.split(GROUP_SEPARATOR).sumBy { answers ->
            answers.toCharArray().filter { it in 'a'..'z' }.distinct().count()
        }
    }

    override fun answerQuestion2(): Int {
        return input.split(GROUP_SEPARATOR).sumBy { answers ->
            val splitAnswers = answers.split(ANSWERS_SEPARATOR)
            splitAnswers.fold(('a'..'z').toSet()) { acc, s ->
                acc.intersect(s.toCharArray().toList())
            }.size
        }
    }
}
