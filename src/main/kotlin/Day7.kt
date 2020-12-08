import java.io.File
import java.lang.IllegalStateException
import java.math.BigInteger
import java.util.*
import kotlin.math.ceil
import kotlin.math.roundToInt

class Day7 : Day<Int, Int> {

    companion object {
        private val GROUP_SEPARATOR = "(.*) bags contain ((.*)[, |.])+".toRegex()
        private val BAGS_CONTENT_SEPARATOR = ", ?".toRegex()
        private val BAG_CONTENT_SEPARATOR = "([0-9]+) (.*) bag[s]?\\.?".toRegex()
    }

    override val name: String = "Day 7"
    private val input = File("src/main/resources/day7.txt").readLines()

    private data class Bag(val color: String, val contents: List<BagContent>) {
        fun containsColor(color: String) = contents.any { content -> content.color == color }
    }

    private data class BagContent(val color: String, val number: Int)

    private fun String.toBag(): Bag {
        val match = GROUP_SEPARATOR.matchEntire(this)
        val color = match!!.groupValues[1]
        val contents = match.groupValues[2].split(BAGS_CONTENT_SEPARATOR).mapNotNull {
            val matches = BAG_CONTENT_SEPARATOR.matchEntire(it)?.groupValues.orEmpty()
            if (matches.size == 3) {
                BagContent(color = matches[2], number = matches[1].toInt())
            } else null
        }

        return Bag(color = color, contents = contents)
    }

    private fun List<Bag>.whichCanContainsColor(color: String): List<Bag> {
        return filter { it.containsColor(color) }
    }

    private fun List<Bag>.find(color: String): List<Bag> {
        return whichCanContainsColor(color).flatMap { bag ->
            find(bag.color) + bag
        }.distinct()
    }

    private fun List<Bag>.contentSizeOf(color: String, number: Int): Int {
        val bag = firstOrNull { it.color == color }
        return number + (bag?.contents?.sumBy { content ->
            val contentSizeOf = contentSizeOf(content.color, content.number)
            if(contentSizeOf >= 1) content.number * contentSizeOf(content.color, 1) else 0
        } ?: 0)
    }

    override fun answerQuestion1(): Int {
        val bags = input.map { it.toBag() }
        return bags.find("shiny gold").size
    }

    override fun answerQuestion2(): Int {
        val bags = input.map { it.toBag() }
        return bags.contentSizeOf("shiny gold", 0)
    }
}
