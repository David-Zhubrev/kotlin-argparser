import com.appdav.argparser.ArgParser
import com.appdav.argparser.ParseResult
import com.appdav.argparser.argument.flags.Flags
import com.appdav.argparser.argument.options.Options
import com.appdav.argparser.argument.options.exclusiveArgument
import com.appdav.argparser.converter.DefaultConverters
import com.appdav.argparser.converter.FileConverter
import com.appdav.argparser.converter.StringConverter
import com.appdav.argparser.registries.ArgRegistry
import com.appdav.argparser.registries.MutuallyExclusiveGroup
import org.junit.jupiter.api.Test
import java.io.File

class OptionsTest {

    private data class OptionTestCase(
        val input: String,
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

    @Test
    fun optionTest() {
        val cases = TestDataProvider.fromFile("/options_test.txt")
        for (case in cases) {
            val reg = object : ArgRegistry() {
                val path = registerMutuallyExclusiveGroup(object : MutuallyExclusiveGroup(true) {
                    val file = this.exclusiveArgument(
                        "--file-path",
                        DefaultConverters.FileConverter()
                    )
                    val folder = this.exclusiveArgument(
                        "--folder-path",
                        DefaultConverters.FileConverter()
                    )
                })
                val nullableOption by Options.nullable("--nullable", DefaultConverters.StringConverter())
                val required by Options.required("--required", DefaultConverters.StringConverter())
                val default by Options.withDefaultValue("--default", DefaultConverters.StringConverter(), "DEFAULT_INIT")
                val verbose by Flags.flag("-v", additionalTokens = listOf("--verbose"), name = "Verbose")
            }
            ArgParser("optiontest", reg).parse(case.input){result ->
                when(case.outputType){
                    TestCase.OutputType.SUCCESS -> {
                        assert(result == ParseResult.Success)
                        when(path.initializedArgument){
                            path.file ->
                                assert(path.file.value == File("C:\\TEST_FILE.test"))
                            path.folder ->
                                assert(path.folder.value == File("C:\\TEST_FOLDER"))
                        }
                        assert(if (case.input.contains("--nullable")) nullableOption == "NULLABLE" else nullableOption == null)
                        assert(required == "REQUIRED")
                        assert(if (case.input.contains("--default")) default == "DEFAULT_INPUT" else default == "DEFAULT_INIT")
                        assert(if (case.input.contains("-v") || case.input.contains("--verbose")) verbose else !verbose)
                    }
                    TestCase.OutputType.EXCEPTION -> {
                        assert(result is ParseResult.Error && result.exception::class.java.simpleName == case.exceptionType)
                    }
                    TestCase.OutputType.MESSAGE -> {
                        assert(result is ParseResult.HelpCommand)
                    }
                    TestCase.OutputType.EMPTY -> {
                        assert(result is ParseResult.EmptyArgs)
                    }
                }

            }
        }
    }


}
