package com.appdav.argparser

import com.appdav.argparser.argument.flags.flag
import com.appdav.argparser.argument.options.Options
import com.appdav.argparser.argument.positional.Positionals
import com.appdav.argparser.converter.DefaultConverters
import com.appdav.argparser.converter.stringConverter
import java.io.File


fun main(args: Array<String>) {

    val registry = object : ArgRegistry() {
        val create = addSubcommand(object : Subcommand("create"){
            val path = Positionals.required("path", DefaultConverters.stringConverter(), 0)
            val verbose = flag("-v")
        })
        val delete = addSubcommand(object : Subcommand("delete"){
            val path = Positionals.required("path", DefaultConverters.stringConverter(), 0)
            val verbose = flag("-v")
        })
        val copy = addSubcommand(object : Subcommand("copy"){
            val from = Positionals.required("from", DefaultConverters.stringConverter(), 0)
            val to = Positionals.required("to", DefaultConverters.stringConverter(), 1)
            val verbose = flag("-v")
        })
        val message = Positionals.required("message", DefaultConverters.stringConverter(), 0)
    }
    ArgParser(registry).parse(args, useDefaultSubcommandIfNone = true){
        when(activeSubcommand){
            create -> create(create.path.value, create.verbose.value)
            delete -> delete(delete.path.value, delete.verbose.value)
            copy -> copy(copy.from.value, copy.to.value, copy.verbose.value)
            else -> message(message.value)
        }
    }
}

fun message(msg: String){
    println("Message: $msg")
}

fun copy(value: String, value1: String, value2: Boolean) {
    println("Copy $value to $value1, verbose: $value2")
}

fun delete(value: String, value1: Boolean) {
    println("Delete file: $value, verbose: $value1")
}

fun create(value: String, value1: Boolean) {
    println("Creating file: $value, verbose: $value1")
}
