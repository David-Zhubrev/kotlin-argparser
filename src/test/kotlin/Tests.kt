import Tests.PlainTest.OutputType.*
import com.appdav.argparser.ArgParser
import com.appdav.argparser.ParseResult
import com.appdav.argparser.argument.positional.Positionals
import com.appdav.argparser.converter.DefaultConverters
import com.appdav.argparser.converter.IntConverter
import com.appdav.argparser.converter.StringConverter
import com.appdav.argparser.exceptions.NonInitializedValueException
import com.appdav.argparser.registries.ArgRegistry
import com.appdav.argparser.registries.Subcommand
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class Tests {

    private data class PlainTest(
        val input: String,
        val outputType: OutputType,
        val outputResult: Int,
    ) {
        enum class OutputType {
            SUCCESS,
            EXCEPTION,
            MESSAGE,
            EMPTY
        }
    }

    @Test
    fun plainRegistryTest() {
        val tests = this::class.java.getResource("/plain_tests.txt").readText()
            .split("\n")
            .filter { it.isNotBlank() }
            .map {
                val splitted = it.split("|")
                PlainTest(splitted[0].trim(), PlainTest.OutputType.valueOf(splitted[1].trim()), splitted[2].trim().toInt())
            }

        for (test in tests) {
            val registry = object : ArgRegistry() {
                val a1 by Positionals.required("a1", DefaultConverters.IntConverter())
                val a2 by Positionals.required("a2", DefaultConverters.IntConverter())
                val a3 by Positionals.nullable("a3", DefaultConverters.IntConverter())
            }
            ArgParser("plaintest", registry)
                .parse(test.input.split(" ")
                    .filter { it.isNotBlank() }
                    .toTypedArray(), false) { result ->
                    when (test.outputType) {
                        SUCCESS ->
                            assert(result == ParseResult.Success && a1 + a2 + (a3 ?: 0) == test.outputResult)
                        EXCEPTION ->
                            assert(result is ParseResult.Error)
                        MESSAGE ->
                            assert(result is ParseResult.HelpCommand)
                        EMPTY ->
                            assert(result is ParseResult.EmptyArgs)
                    }
                }
        }
    }

    @Test
    fun nestedSubcommandsTet() {

        val reg = object : ArgRegistry() {
            val calc = registerSubcommand(object : Subcommand("calc") {
                val plus = registerSubcommand(object : Subcommand("plus") {
                    val a by Positionals.required("a", DefaultConverters.IntConverter(false))
                    val b by Positionals.required("b", DefaultConverters.IntConverter(false))
                })
                val minus = registerSubcommand(object : Subcommand("minus") {
                    val a = Positionals.required("a", DefaultConverters.IntConverter(false))
                    val b by Positionals.required("b", DefaultConverters.IntConverter(false))
                })
            })
            val print = registerSubcommand(object : Subcommand("print") {
                val message by Positionals.required("msg", DefaultConverters.StringConverter())
            })
        }
        val args = arrayOf("print", "helloworld")
        ArgParser("test", reg).parse(args) { result ->
            when (result) {
                is ParseResult.EmptyArgs -> assert(args.isEmpty())
                is ParseResult.Error -> assert(false) { result.exception.message!! }
                is ParseResult.HelpCommand -> {
                    println(result.defaultHelpMessage)
                }

                ParseResult.Success -> {
                    when (reg.activeSubcommand) {
                        calc -> {
                            when (calc.activeSubcommand) {
                                calc.plus -> {
                                    println("calc plus ${calc.plus.a} + ${calc.plus.b}")
                                    assert(calc.plus.a + calc.plus.b == 15)
                                    assertThrows<NonInitializedValueException> { calc.minus.a }
                                }

                                calc.minus -> {
                                    println(
                                        "calc minus ${calc.minus.a} + ${
                                            calc.minus
                                                .b
                                        }"
                                    )
                                    assert(calc.minus.a.value - calc.minus.b == 9)
                                }
                            }
                        }

                        print -> {
                            assert(print.message == "helloworld")
                            println(print.message)
                        }
                    }
                }
            }
        }
    }

//    @Test
//    fun aabArgTest() {
//        val args = arrayOf<String>("help")
//        fun ArgRegistry.keystore() =
//            Options.nullable(
//                "--ks-file", DefaultConverters.FileConverter, additionalTokens = listOf("-ks"),
//                name = "Keystore file"
//            )
//
//        fun ArgRegistry.keystorePass() =
//            Options.nullable(
//                "--ks-pass", DefaultConverters.StringConverter(),
//                name = "Keystore password"
//            )
//
//        fun ArgRegistry.keystoreAlias() =
//            Options.nullable(
//                "--key-alias", DefaultConverters.StringConverter(),
//                name = "Key alias"
//            )
//
//        fun ArgRegistry.keyPass() =
//            Options.nullable(
//                "--key-pass", DefaultConverters.StringConverter(),
//                name = "Key password"
//            )
//
//        val registry = object : RegistryBase() {
////            val group = mutuallyExclusiveGroup(
////                a,b,c,d,e
////            )
//
//            val installApk = addSubcommand(object : Subcommand("apk", "install apk file") {
//                val apk = Positionals.required("Apk file", DefaultConverters.FileConverter)
//                val verbose = flag(
//                    "-v", "Verbose",
//                    description = "Set verbose mode"
//                )
//
//                val keystore = keystore()
//                val keystorePass = keystorePass()
//                val keyAlias = keystoreAlias()
//                val keyPass = keyPass()
//            })
//            val installAab = addSubcommand(object : Subcommand("aab") {
//                val aab = Positionals.required("Aab file", DefaultConverters.FileConverter)
//                val verbose = flag("-v", "Verbose", description = "Set verbose mode")
//
//                val keystore = keystore()
//                val keystorePass = keystorePass()
//                val keyAlias = keystoreAlias()
//                val keyPass = keyPass()
//            })
//            val showDevices = addSubcommand(object : Subcommand("show-devices") {})
//        }
//        ArgParser("testapp", registry).parse(args, false, true) { result ->
//            when (result) {
//                is ParseResult.EmptyArgs -> {
//                    Thread.sleep(1000)
//                    println("Success")
//                    exitProcess(0)
//                }
//
//                is ParseResult.Error -> println("Error: ${result.t.message}")
//                ParseResult.Success -> {
//                    when (activeSubcommand) {
//                        installAab ->
//                            with(installAab) {
//                                println("Installing aab...")
//                            }
//
//                        installApk ->
//                            with(installApk) {
//                                println("installing apk...")
//                            }
//
//                        showDevices -> {
//                            println("devices:")
//                        }
//
//                        else -> {
//                            println("Unknown command")
//                        }
//                    }
//                }
//
//                is ParseResult.HelpCommand -> println(result.defaultHelpMessage)
//            }
//
//        }
//    }

}
