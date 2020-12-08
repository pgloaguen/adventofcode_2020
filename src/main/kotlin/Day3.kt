import java.io.File
import java.math.BigInteger
import kotlin.math.ceil
import kotlin.math.roundToInt

class Day3() : Day<Int, Long> {

    companion object {
        const val Tree = '#'
    }

    override val name: String = "Day 3"

    private val input = File("src/main/resources/day3.txt").readLines()


    override fun answerQuestion1(): Int {
        return input.computeWithSlop(rightSlope = 3, downSlop = 1)
    }

    override fun answerQuestion2(): Long {
        return input.computeWithSlop(rightSlope = 1, downSlop = 1).toLong() *
                input.computeWithSlop(rightSlope = 3, downSlop = 1).toLong() *
                input.computeWithSlop(rightSlope = 5, downSlop = 1).toLong() *
                input.computeWithSlop(rightSlope = 7, downSlop = 1).toLong() *
                input.computeWithSlop(rightSlope = 1, downSlop = 2).toLong()
    }

    private fun List<String>.computeWithSlop(rightSlope: Int, downSlop: Int): Int {
        return foldIndexed(0) { index, acc, line ->
            if (index % downSlop == 0 && index >= downSlop) {
                val position = (index / downSlop) * rightSlope
                val char = line.getOrNull(position % line.length)
                acc + if (char == Tree) 1 else 0
            } else acc
        }
    }
}