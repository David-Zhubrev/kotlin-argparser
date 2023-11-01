object TestDataProvider {

    fun fromFile(filePath: String): List<TestCase> {
        return this::class.java.getResource(filePath).readText()
            .split("\n")
            .filter { it.isNotBlank() }
            .map { line ->
                val split = line.split("|")
                TestCase(
                    split[0].trim().split(" ").filterNot { it.isBlank() }.toTypedArray(),
                    TestCase.OutputType.valueOf(split[1].trim()),
                    split[2].trim().toInt(),
                    split.getOrNull(3)?.trim()
                )
            }
    }

}
