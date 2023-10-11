import com.appdav.argparser.ArgParser
import com.appdav.argparser.ArgRegistry
import com.appdav.argparser.ParseResult
import com.appdav.argparser.Subcommand
import com.appdav.argparser.argument.flags.flag
import com.appdav.argparser.argument.options.Options
import com.appdav.argparser.argument.positional.Positionals
import com.appdav.argparser.converter.DefaultConverters
import com.appdav.argparser.converter.FileConverter
import com.appdav.argparser.converter.StringConverter
import org.junit.jupiter.api.Test
import kotlin.system.exitProcess

class Tests {

    @Test
    fun aabArgTest() {
        val args = arrayOf<String>("help")
        fun ArgRegistry.keystore() =
            Options.nullable(
                "--ks-file", DefaultConverters.FileConverter, additionalTokens = listOf("-ks"),
                name = "Keystore file"
            )

        fun ArgRegistry.keystorePass() =
            Options.nullable(
                "--ks-pass", DefaultConverters.StringConverter(),
                name = "Keystore password"
            )

        fun ArgRegistry.keystoreAlias() =
            Options.nullable(
                "--key-alias", DefaultConverters.StringConverter(),
                name = "Key alias"
            )

        fun ArgRegistry.keyPass() =
            Options.nullable(
                "--key-pass", DefaultConverters.StringConverter(),
                name = "Key password"
            )

        val registry = object : ArgRegistry() {
            val installApk = addSubcommand(object : Subcommand("apk", "install apk file") {
                val apk = Positionals.required("Apk file", DefaultConverters.FileConverter)
                val verbose = flag(
                    "-v", "Verbose",
                    description = "Set verbose mode"
                )

                val keystore = keystore()
                val keystorePass = keystorePass()
                val keyAlias = keystoreAlias()
                val keyPass = keyPass()
            })
            val installAab = addSubcommand(object : Subcommand("aab") {
                val aab = Positionals.required("Aab file", DefaultConverters.FileConverter)
                val verbose = flag("-v", "Verbose", description = "Set verbose mode")

                val keystore = keystore()
                val keystorePass = keystorePass()
                val keyAlias = keystoreAlias()
                val keyPass = keyPass()
            })
            val showDevices = addSubcommand(object : Subcommand("show-devices") {})
        }
        ArgParser("testapp", registry).parse(args, false, true) { result ->
            when (result) {
                is ParseResult.EmptyArgs -> {
                    Thread.sleep(1000)
                    println("Success")
                    exitProcess(0)
                }

                is ParseResult.Error -> println("Error: ${result.t.message}")
                ParseResult.Success -> {
                    when (activeSubcommand) {
                        installAab ->
                            with(installAab) {
                                println("Installing aab...")
                            }

                        installApk ->
                            with(installApk) {
                                println("installing apk...")
                            }

                        showDevices -> {
                            println("devices:")
                        }

                        else -> {
                            println("Unknown command")
                        }
                    }
                }

                is ParseResult.HelpCommand -> println(result.defaultHelpMessage)
            }

        }
    }

}
