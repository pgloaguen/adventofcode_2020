import java.io.File
import java.lang.IllegalStateException
import java.math.BigInteger
import java.util.*
import kotlin.math.ceil
import kotlin.math.roundToInt

class Day5 : Day<Int, Int> {

    override val name: String = "Day 5"
    private val input = File("src/main/resources/day5.txt").readLines()

    override fun answerQuestion1(): Int {
        return input.maxOf {
            it.fold(0) { acc, c -> (acc shl 1) + if (c == 'B' || c == 'R') 1 else 0 }
        }
    }

    override fun answerQuestion2(): Int {
        val rowWithOnePlaceLeft = input
            .map { it.fold(0) { acc, c -> (acc shl 1) + if (c == 'B' || c == 'R') 1 else 0 } }
            .groupBy({ it shr 3 }, { it % 8 })
            .filterValues { it.size == 7 }


        val row = rowWithOnePlaceLeft.keys.first()
        val column = (0 until 8).subtract(rowWithOnePlaceLeft.getValue(row)).first()
        return row * 8 + column
    }
}
