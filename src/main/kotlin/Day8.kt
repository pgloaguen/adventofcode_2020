import java.io.File
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class Day8 : Day<Int, Int> {

    companion object {
        private const val OP_NOT = "nop"
        private const val OP_ACC = "acc"
        private const val OP_JMP = "jmp"
    }

    private val input = File("src/main/resources/day8.txt").readLines()

    override val name: String = "Day 8"

    private sealed class Command {

        data class Result(val inc: Int, val index: Int)

        abstract fun execute(currentResult: Result): Result

        object END : Command() {
            override fun execute(currentResult: Result): Result {
                return currentResult
            }
        }

        data class NOT(val value: Int) : Command() {
            override fun execute(currentResult: Result): Result {
                return currentResult.copy(index = currentResult.index + 1)
            }
        }

        data class JMP(val value: Int) : Command() {
            override fun execute(currentResult: Result): Result {
                return currentResult.copy(index = currentResult.index + value)
            }
        }

        data class ACC(val inc: Int) : Command() {
            override fun execute(currentResult: Result): Result {
                return Result(currentResult.inc + inc, index = currentResult.index + 1)
            }
        }
    }

    private fun String.toCommand() = with(split(" ")) {
        val value = get(1).toInt()
        when (get(0)) {
            OP_NOT -> Command.NOT(value)
            OP_JMP -> Command.JMP(value)
            OP_ACC -> Command.ACC(value)
            else -> throw IllegalArgumentException("${get(0)} command unknown")
        }
    }

    private fun MutableList<Command>.execute(previousResults: List<Command.Result>): List<Command.Result> {
        val previousResult = previousResults.last()
        val command = get(previousResult.index)
        val nextResult = command.execute(previousResult)
        set(previousResult.index, Command.END)
        val nextCommand = getOrNull(nextResult.index)
        return if (nextCommand == Command.END || nextCommand == null) previousResults + nextResult
        else execute(previousResults + nextResult)
    }

    override fun answerQuestion1(): Int {
        val commands = input.map { it.toCommand() }.toMutableList()
        return commands.execute(listOf(Command.Result(0, 0))).last().inc
    }

    override fun answerQuestion2(): Int {
        val commands = input.map { it.toCommand() }
        val results = commands.toMutableList().execute(listOf(Command.Result(0, 0)))
        return results.reversed().asSequence()
            .filter { commands[it.index] is Command.JMP || commands[it.index] is Command.NOT }
            .mapNotNull {
                val fixedCommands = commands.toMutableList().apply {
                    when (val command = commands[it.index]) {
                        is Command.NOT -> set(it.index, Command.JMP(command.value))
                        is Command.JMP -> set(it.index, Command.NOT(command.value))
                        else -> throw IllegalStateException()
                    }
                }

                val programResults = fixedCommands.execute(listOf(Command.Result(0, 0)))

                programResults.last().takeIf { it.index - 1 == commands.lastIndex }?.inc
            }.first()
    }
}
