import java.io.File

class Day1 : Day<Int, Int> {

    override val name: String
        get() = "Day 1"

    private val input = File("src/main/resources/day1.txt").readLines()

    override fun answerQuestion1(): Int {
        val entries = input.map { it.toInt() }
        entries.forEach { e1 ->
            entries.forEach { e2 ->
                if (e1 + e2 == 2020) {
                    return e1 * e2
                }
            }
        }
        throw IllegalStateException("No results found")
    }

    override fun answerQuestion2(): Int {
        val entries = input.map { it.toInt() }.sorted()
        for (e1 in entries) {
            loop2@ for (e2 in entries) {
                if (e1 + e2 >= 2020) break@loop2
                for (e3 in entries) {
                    if (e1 + e2 + e3 == 2020) {
                        return e1 * e2 * e3
                    }
                }
            }
        }
        throw IllegalStateException("No results found")
    }


}