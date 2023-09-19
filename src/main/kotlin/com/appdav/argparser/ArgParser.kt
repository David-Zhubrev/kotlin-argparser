package com.appdav.argparser

import com.appdav.argparser.argument.flags.Flag
import com.appdav.argparser.argument.options.NullableOption
import com.appdav.argparser.argument.positional.NullablePositional


class ArgParser<T : ArgRegistry>(
    private val registry: T,
) {

    fun parse(
        args: Array<String>,
        ignoreUnknownOptions: Boolean = false,
    ) {

        val flags = registry.filterIsInstance<Flag>()
        val options = registry.filterIsInstance<NullableOption<*>>()
        val positionals = registry.filterIsInstance<NullablePositional<*>>().sortedBy { it.position }
        val mutableArgs = args.toMutableList()

        parseFlags(flags, mutableArgs, ignoreUnknownOptions)
        parseOptions(options, mutableArgs, ignoreUnknownOptions)
        if (!ignoreUnknownOptions) {
            checkForUnknownOptionsAndFlags(mutableArgs)
        }
        parsePositionals(positionals, mutableArgs, ignoreUnknownOptions)
        validateAll()
    }


    private fun checkForUnknownOptionsAndFlags(mutableArgs: MutableList<String>) {
        val unknownOptionsAndFlags = mutableListOf<String>()
        for (arg in mutableArgs) {
            if (arg.startsWith("-")) {
                unknownOptionsAndFlags.add(arg)
            }
        }
        if (unknownOptionsAndFlags.isNotEmpty()) {
            throw IllegalArgumentException("Unknown arguments: ${unknownOptionsAndFlags.joinToString("\n") { it }}")
        }
    }

    fun parse(args: Array<String>, onParse: T.() -> Unit) {
        parse(args)
        onParse(registry)
    }

    private fun validateAll() {
        for (arg in registry) {
            if (!arg.isParsed) continue
            else {
                try {
                    val isValid = arg.validate()
                    if (!isValid) throw IllegalStateException("Argument $arg has invalid value: ${arg.value}")
                } catch (e: Exception) {
                    throw IllegalStateException("Argument ${arg.name} has invalid value: ${arg.value}")
                }
            }
        }
    }

    private fun parsePositionals(
        positionals: List<NullablePositional<*>>,
        mutableArgs: MutableList<String>,
        ignoreUnknownOptions: Boolean,
    ) {
        if (mutableArgs.size > positionals.size &&
            !ignoreUnknownOptions
        ) {
            throw IllegalStateException("Provided positionals arguments count is more than registered argument count")
        }

        val iterator = mutableArgs.iterator()
        outer@ for (positional in positionals) {
            if (iterator.hasNext()) {
                val next = iterator.next()
                //TODO: wrap with try-catch
                positional.parseInput(next)
                continue@outer
            } else {
                break@outer
            }
        }
        val nonInitialized = positionals.filter { !it.isParsed && it.required }
        if (nonInitialized.isNotEmpty()) throw IllegalStateException(
            "No value passed for positional parameters: " + nonInitialized.joinToString(
                ", "
            ) { it.name })
    }


    private fun parseOptions(
        options: List<NullableOption<*>>,
        mutableArgs: MutableList<String>,
        ignoreUnknownOptions: Boolean,
    ) {
        outer@ for (option in options) {
            val allTokens = option.additionalTokens + option.token
            for (token in allTokens) {
                val arg = mutableArgs.find { it in token }
                if (arg != null) {
                    val value =
                        mutableArgs.getOrNull(mutableArgs.indexOf(arg) + 1)?.takeIf { !it.startsWith("-") }
                            ?: ""
                    option.parseInput(value)
                    mutableArgs.remove(arg)
                    mutableArgs.remove(value)
                    continue@outer
                }
            }
            //TODO: make my own exception
            if (option.required && !option.isParsed) throw IllegalStateException("Couldn't find required option ${option.name}")
        }
        if (!ignoreUnknownOptions) {
            for (arg in mutableArgs) {
                if (arg.startsWith("--")) {
                    val option = options.find { arg in it.allTokens() }
                    if (option == null) {
                        throw IllegalStateException("Unknown option: $arg")
                    } else {
                        option.parseInput(mutableArgs.getOrNull(mutableArgs.indexOf(arg) + 1) ?: "")
                    }
                }
            }
        }
    }

    private fun parseFlags(
        flags: List<Flag>,
        mutableArgs: MutableList<String>,
        ignoreUnknownOptions: Boolean,
    ) {
        outer@ for (flag in flags) {
            for (token in flag.allTokens()) {
                val arg = mutableArgs.find { it in token }
                if (arg != null) {
                    flag.parseInput("")
                    mutableArgs.remove(arg)
                    continue@outer
                }
            }
        }
    }


}
