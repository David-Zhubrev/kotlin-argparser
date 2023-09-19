//package com.appdav.argparser
//
//import com.appdav.argparser.argument.*
//import com.appdav.argparser.argument.flags.flag
//import com.appdav.argparser.argument.options.Options
//import com.appdav.argparser.argument.positional.Positionals
//import java.io.File
//
//val StringConverter = ValueConverter {
//    it.trim()
//}
//
//val BooleanConverter = ValueConverter {
//    if (it.isBlank()) true
//    else when (it) {
//        "y", "yes", "true" -> true
//        "n", "no", "false" -> false
//        else -> throw IllegalArgumentException("Unknown boolean argument value: $it")
//    }
//}
//
//
//fun main(args: Array<String>) {
//
//    val registry = object : ArgRegistry() {
//
//        val mode = Positionals.withDefaultValue(
//            "Mode",
//            converter = StringConverter,
//            position = 0,
//            validator = { value ->
//                when (value) {
//                    "append" -> true
//                    "clear" -> true
//                    else -> false
//                }
//            },
//            defaultValue = "append",
//        )
//
//        val verbose = flag("-v")
//
//        val file = Options.required(
//            "-f",
//            additionalTokens = listOf("--file"),
//            name = "file",
//            converter = { File(it) },
//            validator = { it?.isFile == true },
//        )
//    }
//
//    val sub1 = object : Subcommand
//    registry.addSubcommand("copy"){
//        val a = Positionals.required()
//    }
//
//    ArgParser(registry).parse(args) {
//        if (verbose.value) {
//            println("Mode: ${mode.value}, file=${file.value}")
//        } else {
//            println("${mode.value} ${file.value}")
//        }
//    }
//}
