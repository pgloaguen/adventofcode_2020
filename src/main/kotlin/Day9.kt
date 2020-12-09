import java.io.File
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.math.max
import kotlin.math.min

class Day9 : Day<Long, Long> {

    companion object {
    }

    private val input = File("src/main/resources/day9.txt").readLines()

    override val name: String = "Day 9"

    private fun List<Long>.findFirstNumberWhichNotMatch(preambleSize: Int): Long {
        val indexOfNumberWhichDoesNotMatch =
            (0..lastIndex).first { index ->
                val valueToCheck = get(index + preambleSize)
                (index until index + preambleSize)
                    .asSequence()
                    .flatMap { i -> (i + 1 until index + preambleSize).asSequence().map { j -> i to j } }
                    .firstOrNull { (i, j) -> valueToCheck == get(i) + get(j) } == null
            } + preambleSize

        return get(indexOfNumberWhichDoesNotMatch)
    }

    override fun answerQuestion1(): Long {
        return input.map { it.toLong() }.findFirstNumberWhichNotMatch(25)
    }

    override fun answerQuestion2(): Long {
        val integers = input.map { it.toLong() }
        val numberWhichDoesNotMatch: Long = input.map { it.toLong() }.findFirstNumberWhichNotMatch(25)
        val maxIndex = integers.indexOf(numberWhichDoesNotMatch)
        (0 until maxIndex).forEach { firstIndex ->
            (firstIndex + 1 until maxIndex).forEach { lastIndex ->
                val subset = integers.subList(firstIndex, lastIndex)
                if (subset.sum() == numberWhichDoesNotMatch) return subset.minOrNull()!! + subset.maxOrNull()!!
            }
        }
        return -1
    }
}
