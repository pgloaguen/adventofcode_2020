import org.w3c.dom.traversal.TreeWalker
import java.io.File
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*
import kotlin.math.max
import kotlin.math.min

class Day10 : Day<Int, Long> {

    private val input = File("src/main/resources/day10.txt").readLines()
        .map { it.toInt() }.sorted()

    override val name: String = "Day 10"


    override fun answerQuestion1(): Int {
        val joltageRatings = input

        var countOf1Diff = 0
        var countOf3Diff = 1
        var jolt = 0

        joltageRatings.forEach { joltRating ->
            val countDiff = (joltRating - jolt)
            if (countDiff == 1) countOf1Diff++
            else if (countDiff == 3) countOf3Diff++

            jolt += countDiff
        }

        println("countOf1Diff: $countOf1Diff")
        println("countOf3Diff: $countOf3Diff")
        return countOf1Diff * countOf3Diff
    }


    // For this one I found the solution here: https://todd.ginsberg.com/post/advent-of-code/2020/day10/
    // Shame on me
    override fun answerQuestion2(): Long {
        val pathsByAdapter: MutableMap<Int, Long> = mutableMapOf(0 to 1L)
        val adapters: List<Int> = listOf(0) + input + (input.maxOrNull()!! + 3)

        adapters.drop(1).forEach { adapter ->
            pathsByAdapter[adapter] = (1..3).map { lookBack ->
                pathsByAdapter.getOrDefault(adapter - lookBack, 0)
            }.sum()
        }

        return pathsByAdapter.getValue(adapters.last())
    }
}
