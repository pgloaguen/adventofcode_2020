import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
fun printMeasureNanoTime(block: () -> String) {
    var message: String?
    val time = measureTime {
        message = block()
    }
    println("$message in $time")
}

@ExperimentalTime
fun main() {
    try {
        println("Start Santa Challenge !")
        val time = measureTime {
            executeDay(Day1())
            executeDay(Day2())
            executeDay(Day3())
            executeDay(Day4())
            executeDay(Day5())
            executeDay(Day6())
            executeDay(Day7())
            executeDay(Day8())
            executeDay(Day9())
            executeDay(Day10())
            executeDay(Day11())
            executeDay(Day12())
        }
        println("-------")
        println("All days in $time")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@ExperimentalTime
fun executeDay(day: Day<Any, Any>) {
    with(day) {
        println("-- $name --")
        printMeasureNanoTime { "Answer question 1: " + day.answerQuestion1() }
        printMeasureNanoTime { "Answer question 2: " + day.answerQuestion2() }
    }
}