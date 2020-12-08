import java.io.File

class Day2 : Day<Int, Int> {

    override val name: String
        get() = "Day 2"

    private val input = File("src/main/resources/day2.txt").readLines()

    override fun answerQuestion1(): Int {
        val inputRegex = "([0-9]+)-([0-9]+) ([a-z]): ([a-z]+)".toRegex()
        return input.filter {
            val results = inputRegex.find(it)!!.groupValues
            val bounds = (results[1].toInt()..results[2].toInt())
            bounds.contains(results[4].filter { c -> c == results[3][0] }.length)
        }.count()
    }

    override fun answerQuestion2(): Int {
        val inputRegex = "([0-9]+)-([0-9]+) ([a-z]): ([a-z]+)".toRegex()
        return input.filter {
            val results = inputRegex.find(it)!!.groupValues
            val car1 = results[4][results[1].toInt() - 1]
            val car2 = results[4][results[2].toInt() - 1]
            val letter = results[3][0]

            car1 != car2 && (car1 == letter || car2 == letter)
        }.count()
    }
}