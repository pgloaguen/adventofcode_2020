import org.w3c.dom.traversal.TreeWalker
import java.io.File
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*
import kotlin.math.max
import kotlin.math.min

class Day11 : Day<Int, Int> {

    companion object {
        private const val NONE = '.'
        private const val EMPTY = 'L'
        private const val OCCUPIED = '#'
    }

    private val input = File("src/main/resources/day11.txt").readLines().map {
        it.toCharArray()
    }.toTypedArray()

    override val name: String = "Day 11"

    interface NearSeatMapCreator {
        fun create(input: Array<CharArray>, row: Int, column: Int): List<Pair<Int, Int>>

        object Basic : NearSeatMapCreator {
            override fun create(input: Array<CharArray>, row: Int, column: Int): List<Pair<Int, Int>> {
                return (row - 1..row + 1).flatMap { r ->
                    (column - 1..column + 1).filterNot { row == r && it == column }.mapNotNull { c ->
                        if (r >= 0 && c >= 0 && c < input[row].size && r < input.size)
                            r to c else null
                    }
                }
            }
        }

        object Larger : NearSeatMapCreator {
            override fun create(input: Array<CharArray>, row: Int, column: Int): List<Pair<Int, Int>> {
                val toLeft = if (column == 0) IntRange.EMPTY else (0 until column).reversed()
                val toRight = if (column == input[row].lastIndex) IntRange.EMPTY else (column + 1..input[row].lastIndex)
                val toTop = if (row == 0) IntRange.EMPTY else (0 until row).reversed()
                val toBottom = if (row == input.lastIndex) IntRange.EMPTY else (row + 1..input.lastIndex)
                val toDiagonalTopLeft = toTop.zip(toLeft)
                val toDiagonalBottomLeft = toBottom.zip(toLeft)
                val toDiagonalBottomRight = toBottom.zip(toRight)
                val toDiagonalTopRight = toTop.zip(toRight)


                return listOfNotNull(
                    toLeft.asSequence().mapNotNull { c -> if (input[row][c] != NONE) row to c else null }.firstOrNull(),
                    toRight.asSequence().mapNotNull { c -> if (input[row][c] != NONE) row to c else null }
                        .firstOrNull(),
                    toTop.asSequence().mapNotNull { r -> if (input[r][column] != NONE) r to column else null }
                        .firstOrNull(),
                    toBottom.asSequence().mapNotNull { r -> if (input[r][column] != NONE) r to column else null }
                        .firstOrNull(),
                    toDiagonalTopLeft.firstOrNull { (r, c) -> input[r][c] != NONE },
                    toDiagonalBottomLeft.firstOrNull { (r, c) -> input[r][c] != NONE },
                    toDiagonalBottomRight.firstOrNull { (r, c) -> input[r][c] != NONE },
                    toDiagonalTopRight.firstOrNull { (r, c) -> input[r][c] != NONE },
                )
            }
        }
    }

    private fun emptyPattern(
        nearSeatsMap: Map<Pair<Int, Int>, List<Pair<Int, Int>>>,
        input: Array<CharArray>,
        row: Int,
        column: Int
    ): Char {
        val nearSeats = nearSeatsMap.getValue(row to column)
        val noneOccupiedSeats = nearSeats.none { (r, c) -> input[r][c] == OCCUPIED }
        return if (noneOccupiedSeats) OCCUPIED else EMPTY
    }

    private fun occupiedPattern(
        nearSeatsMap: Map<Pair<Int, Int>, List<Pair<Int, Int>>>,
        input: Array<CharArray>,
        row: Int,
        column: Int,
        maxOccupiedSeat: Int
    ): Char {
        val nearSeats = nearSeatsMap.getValue(row to column)
        val numbersOfOccupiedSeats = nearSeats.count { (r, c) -> input[r][c] == OCCUPIED }
        return if (numbersOfOccupiedSeats >= maxOccupiedSeat) EMPTY else OCCUPIED
    }

    private fun computeNewState(
        nearSeatMapCreator: NearSeatMapCreator,
        nearSeatsMap: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>>,
        input: Array<CharArray>,
        maxOccupiedSeat: Int
    ): Array<CharArray> {
        val newState = Array(input.size, init = { CharArray(input[0].size, init = { NONE }) })
        input.forEachIndexed { row, chars ->
            chars.forEachIndexed { column, char ->
                if (nearSeatsMap[row to column] == null) nearSeatsMap[row to column] =
                    nearSeatMapCreator.create(input, row, column)
                when (char) {
                    EMPTY -> newState[row][column] = emptyPattern(nearSeatsMap, input, row, column)
                    OCCUPIED -> newState[row][column] =
                        occupiedPattern(nearSeatsMap, input, row, column, maxOccupiedSeat)
                    else -> NONE
                }
            }
        }

        return newState
    }

    private fun Array<CharArray>.print() {
        forEach { println(it.contentToString()) }

        println((0..lastIndex * 2).joinToString(separator = "") { "=" })
    }

    override fun answerQuestion1(): Int {
        var oldSeatsState: Array<CharArray> = emptyArray()
        var seatsState: Array<CharArray> = input
        var count = 0
        val nearSeatsMap: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>> = mutableMapOf()
        while (!seatsState.contentDeepEquals(oldSeatsState)) {
            oldSeatsState = seatsState
            seatsState = computeNewState(NearSeatMapCreator.Basic, nearSeatsMap, seatsState, 4)
            count++
        }

        return seatsState.sumBy { it.count { c -> c == OCCUPIED } }
    }


    override fun answerQuestion2(): Int {
        var oldSeatsState: Array<CharArray> = emptyArray()
        var seatsState: Array<CharArray> = input
        var count = 0
        val nearSeatsMap: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>> = mutableMapOf()
        while (!seatsState.contentDeepEquals(oldSeatsState)) {
            oldSeatsState = seatsState
            seatsState = computeNewState(NearSeatMapCreator.Larger, nearSeatsMap, seatsState, 5)
            count++
        }

        return seatsState.sumBy { it.count { c -> c == OCCUPIED } }
    }
}
