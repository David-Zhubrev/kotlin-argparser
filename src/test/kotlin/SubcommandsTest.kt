import com.appdav.argparser.ArgParser
import com.appdav.argparser.ParseResult
import com.appdav.argparser.argument.flags.flag
import com.appdav.argparser.argument.options.Options
import com.appdav.argparser.argument.positional.Positionals
import com.appdav.argparser.converter.DefaultConverters
import com.appdav.argparser.converter.FloatConverter
import com.appdav.argparser.converter.StringConverter
import com.appdav.argparser.registries.ArgRegistry
import com.appdav.argparser.registries.FlagRegistryScope
import com.appdav.argparser.registries.PositionalRegistryScope
import com.appdav.argparser.registries.Subcommand
import org.junit.jupiter.api.Test

class SubcommandsTest {


    @Test
    fun subcommandsTest() {
        fun FlagRegistryScope.double() = flag("-d", "double")
        fun PositionalRegistryScope.a1() = Positionals.required("a1", DefaultConverters.FloatConverter())
        fun PositionalRegistryScope.a2() = Positionals.required("a2", DefaultConverters.FloatConverter())

        val cases = TestDataProvider.fromFile("/subcommands_test.txt")
        for (case in cases) {
            val reg = object : ArgRegistry(true) {
                val add = registerSubcommand(object : Subcommand("add") {
                    val a1 by a1()
                    val a2 by a2()
                    val double by double()
                })
                val subtract = registerSubcommand(object : Subcommand("subtract") {
                    val a1 by a1()
                    val a2 by a2()
                    val double by double()
                })
                val multiply = registerSubcommand(object : Subcommand("multiply") {
                    val a1 by a1()
                    val a2 by a2()
                    val double by double()
                })
                val divide = registerSubcommand(object : Subcommand("divide") {
                    val a1 by a1()
                    val a2 by a2()
                })
                val nested = registerSubcommand(object : Subcommand("nested", useDefaultSubcommandIfNone = false) {
                    val showMessage = registerSubcommand(object : Subcommand("show-message") {
                        val msg by Options.required(
                            "-m",
                            additionalTokens = listOf("--message"),
                            converter = DefaultConverters.StringConverter()
                        )
                    })
                })
                val a1 by a1()
                val a2 by a2()
            }
            ArgParser("test", reg).parse(case.input) { result ->
                when (case.outputType) {
                    TestCase.OutputType.SUCCESS -> {
                        when (activeSubcommand) {
                            add -> {
                                val sum = (add.a1 + add.a2).let { if (add.double) it * 2 else it }
                                assert(sum == case.outputResult.toFloat())
                            }

                            subtract -> {
                                val dif = (subtract.a1 - subtract.a2).let { if (subtract.double) it * 2 else it }
                                assert(dif == case.outputResult.toFloat())
                            }

                            multiply -> {
                                val mul = (multiply.a1 * multiply.a2).let { if (multiply.double) it * 2 else it }
                                assert(mul == case.outputResult.toFloat())
                            }

                            divide -> {
                                val div = (divide.a1 / divide.a2)
                                assert(div == case.outputResult.toFloat())
                            }
                            nested ->{
                                with(nested){
                                    when(activeSubcommand){
                                        showMessage -> assert(showMessage.msg == "MESSAGE")
                                    }
                                }
                            }

                            else -> {
                                val res = a1 * 10 + a2 * 10
                                assert(res == case.outputResult.toFloat())
                            }
                        }
                    }

                    TestCase.OutputType.EXCEPTION -> {
                        assert(result is ParseResult.Error && case.exceptionType == result.exception::class.simpleName)
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
