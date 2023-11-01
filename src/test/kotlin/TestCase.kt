@Suppress("ArrayInDataClass")
data class TestCase(
    val input: Array<String>,
    val outputType: OutputType,
    val outputResult: Int,
    val exceptionType: String?,
) {
    enum class OutputType {
        SUCCESS,
        EXCEPTION,
        MESSAGE,
        EMPTY
    }
}
