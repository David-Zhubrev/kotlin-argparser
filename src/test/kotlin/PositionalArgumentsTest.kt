import com.appdav.argparser.ArgParser
import com.appdav.argparser.ParseResult
import com.appdav.argparser.argument.positional.Positionals
import com.appdav.argparser.converter.DefaultConverters
import com.appdav.argparser.converter.IntConverter
import com.appdav.argparser.registries.ArgRegistry
import org.junit.jupiter.api.Test

class PositionalArgumentsTest {

    @Test
    fun positionalArgumentsTest() {
        val testCases = TestDataProvider.fromFile("/positional_arg_tests.txt")
        for (test in testCases) {
            val registry = object : ArgRegistry() {
                val a1 by Positionals.required("a1", DefaultConverters.IntConverter())
                val a2 by Positionals.required("a2", DefaultConverters.IntConverter())
                val a3 by Positionals.withDefaultValue("a3", DefaultConverters.IntConverter(), 5)
                val a4 by Positionals.nullable("a4", DefaultConverters.IntConverter())
            }
            ArgParser("postionaltest", registry)
                .parse(test.input, false) { result ->
                    when (test.outputType) {
                        TestCase.OutputType.SUCCESS -> assert(
                            result == ParseResult.Success && a1 + a2 + a3 + (a4 ?: 0) == test.outputResult
                        )
                        TestCase.OutputType.EXCEPTION -> {
                            assert(result is ParseResult.Error && result.exception::class.simpleName == test.exceptionType)
                        }
                        TestCase.OutputType.MESSAGE -> assert(result is ParseResult.HelpCommand)
                        TestCase.OutputType.EMPTY -> assert(result is ParseResult.EmptyArgs)
                    }
                }
        }
    }

}
