interface Day<out A, out B> {
    val name: String
    fun answerQuestion1(): A
    fun answerQuestion2(): B
}