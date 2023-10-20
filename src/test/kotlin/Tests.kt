import com.appdav.argparser.ArgParser
import com.appdav.argparser.registries.RegistryBase
import com.appdav.argparser.ParseResult
import com.appdav.argparser.registries.Subcommand
import com.appdav.argparser.argument.flags.flag
import com.appdav.argparser.argument.options.Options
import com.appdav.argparser.argument.positional.Positionals
import com.appdav.argparser.converter.DefaultConverters
import com.appdav.argparser.converter.FileConverter
import com.appdav.argparser.converter.IntConverter
import com.appdav.argparser.converter.StringConverter
import com.appdav.argparser.registries.MutuallyExclusiveGroup
import org.junit.jupiter.api.Test
import kotlin.system.exitProcess

class Tests {

    @Test
    fun nestedSubcommandsTet(){
        val reg = object : RegistryBase(){
            val calc = addSubcommand(object : Subcommand("calc"){

                val plus = addSubcommand(object : Subcommand("plus"){
                    val a by Positionals.required("a", DefaultConverters.IntConverter(false))
                    val b by Positionals.required("b", DefaultConverters.IntConverter(false))
                })

                val minus = addSubcommand(object: Subcommand("minus"){
                    val a by Positionals.required("a", DefaultConverters.IntConverter(false))
                    val b by Positionals.required("b", DefaultConverters.IntConverter(false))
                })
            })
            val print = addSubcommand(object: Subcommand("print"){
                val message by Positionals.required("msg", DefaultConverters.StringConverter())
            })
        }

//        val reg = object : ArgRegistry(){
//            val suka = addMutuallyExclusiveGroup(object : MutuallyExclusiveGroup(){
//              val a = this.Positionals.required("suka", DefaultConverters.StringConverter())
//            })
//        }

        val args = arrayOf("calc help")
        ArgParser("test", reg).parse(args){result ->
            when(result){
                is ParseResult.EmptyArgs -> assert(args.isEmpty())
                is ParseResult.Error -> assert(false){result.t.message!!}
                is ParseResult.HelpCommand -> {
                    println(result.defaultHelpMessage)
                }
                ParseResult.Success -> {
                    when(reg.activeSubcommand){
                        calc -> {
                            when (calc.activeSubcommand){
                                calc.plus -> {
                                    assert(calc.plus.a + calc.plus.b == 15)
                                }
                                calc.minus -> {
                                    assert(calc.minus.a - calc.minus.b == 9)
                                }
                            }
                        }
                        print -> {
                            assert(print.message == "helloworld")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun aabArgTest() {
        val args = arrayOf<String>("help")
        fun RegistryBase.keystore() =
            Options.nullable(
                "--ks-file", DefaultConverters.FileConverter, additionalTokens = listOf("-ks"),
                name = "Keystore file"
            )

        fun RegistryBase.keystorePass() =
            Options.nullable(
                "--ks-pass", DefaultConverters.StringConverter(),
                name = "Keystore password"
            )

        fun RegistryBase.keystoreAlias() =
            Options.nullable(
                "--key-alias", DefaultConverters.StringConverter(),
                name = "Key alias"
            )

        fun RegistryBase.keyPass() =
            Options.nullable(
                "--key-pass", DefaultConverters.StringConverter(),
                name = "Key password"
            )

        val registry = object : RegistryBase() {
//            val group = mutuallyExclusiveGroup(
//                a,b,c,d,e
//            )

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
