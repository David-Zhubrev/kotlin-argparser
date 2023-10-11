package com.appdav.argparser

import com.appdav.argparser.argument.flags.Flag
import com.appdav.argparser.argument.options.NullableOption
import com.appdav.argparser.argument.positional.NullablePositional
import com.appdav.argparser.exceptions.NoSubcommandFoundException
import com.appdav.argparser.exceptions.TooManyPositionalArgumentsException
import com.appdav.argparser.exceptions.UnknownTokenException
import kotlin.jvm.Throws


/**
 * Argument parser that handles the parsing process
 */
class ArgParser<T : ArgRegistry>(
    private val appName: String,
    private val argRegistry: T,
    private val helpArguments: List<String> = listOf("help", "-h", "--help")
) {

     /**
     * Checks if some of the registered subcommand is called
     * @param subcommands list of registered subcommands
     * @param args command-line arguments
     * @return Subcommand instance that should be parsed, or null if none is specified or there are no subcommands registered
     * @see Subcommand
     */
    private fun acquireCurrentSubcommand(subcommands: List<Subcommand>, args: List<String>): Subcommand? {
        if (args.isEmpty()) return null
        return subcommands.find { it.name == args.first() }
    }

    private fun String.isHelpCommand(): Boolean{
        return this in helpArguments
    }

    /**
     * Parses the registered arguments and subcommands. Firstly, it tries to figure out if any of the subcommands is called.
     * After that it parses the arguments in the following order: flags, options and then the positionals. For each type of arguments it checks if some of the required arguments
     * are missing and throws the RequiredArgumentMissingException if there are some.
     * After the parsing process it calls the validation function for each of the argument.
     * @param args command-line arguments array passed into the main function
     * @param ignoreUnknownOptions if true, it ignores any unknown flags and options, which are denoted by unregistered tokens. If false, throws an
     * UnknownTokenException. False by default.
     * @param useDefaultSubcommandIfNone if true, uses the root arguments of provided ArgRegistry as a default subcommand, if none is present. If False, throws NoSubcommandFoundException, if no subcommand is recognized.
     * True by default
     */

    fun parse(
        args: Array<String>,
        ignoreUnknownOptions: Boolean = false,
        useDefaultSubcommandIfNone: Boolean = true,
    ): ParseResult {
        val helpMessageCreator = HelpMessageCreator(appName, argRegistry)
        if (args.isEmpty()) {
            return ParseResult.EmptyArgs(helpMessageCreator.createMessageForNoArgs())
        }
        val mutableArgs = args.toMutableList()

        val subcommands = argRegistry.subcommands
        var currentRegistry: ArgRegistry = argRegistry
        if (subcommands.isNotEmpty()) {
            val currentSubcommand = acquireCurrentSubcommand(subcommands, mutableArgs)
            if (currentSubcommand == null && !useDefaultSubcommandIfNone) {
                return if (mutableArgs.isNotEmpty() && mutableArgs.first().isHelpCommand()){
                    ParseResult.HelpCommand(helpMessageCreator.createDefaultHelpMessage(false))
                } else {
                    ParseResult.Error(NoSubcommandFoundException(mutableArgs.first()))
                }
            }
            if (currentSubcommand != null) {
                currentRegistry = currentSubcommand
                mutableArgs.removeFirst()
                argRegistry.setActiveSubcommand(currentSubcommand)
            }
        }

        if (mutableArgs.size > 0) {
            val next = mutableArgs.first()
            if (next.isHelpCommand()) {
                return if (currentRegistry is Subcommand){
                    ParseResult.HelpCommand(helpMessageCreator.createSubcommandHelpMessage(currentRegistry))
                } else {
                    ParseResult.HelpCommand(helpMessageCreator.createDefaultHelpMessage(useDefaultSubcommandIfNone))
                }
            }
        }

        val flags = currentRegistry.flags()
        val options = currentRegistry.options()
        val positionals = currentRegistry.positionals()

        parseFlags(flags, mutableArgs)
        parseOptions(options, mutableArgs)?.let { error -> return error }
        if (!ignoreUnknownOptions) {
            checkForUnknownOptionsAndFlags(mutableArgs)?.let { error -> return error }
        }
        parsePositionals(positionals, mutableArgs, ignoreUnknownOptions)?.let { error -> return error }
        validateAll(currentRegistry)?.let { error -> return error }
        return ParseResult.Success
    }


    /**
     * Checks for unknown options and flags withing passed arguments
     */
    private fun checkForUnknownOptionsAndFlags(mutableArgs: MutableList<String>): ParseResult.Error? {
        val unknownOptionsAndFlags = mutableListOf<String>()
        for (arg in mutableArgs) {
            if (arg.startsWith("-")) {
                unknownOptionsAndFlags.add(arg)
            }
        }
        if (unknownOptionsAndFlags.isNotEmpty()) {
            return ParseResult.Error(UnknownTokenException(unknownOptionsAndFlags))
        }
        return null
    }

    /**
     * Parses the provided command-line arguments and calls provided registry-scoped onParse action after parsing process
     * @see parse
     */
    fun parse(
        args: Array<String>,
        ignoreUnknownOptions: Boolean = false,
        useDefaultSubcommandIfNone: Boolean = true,
        onParse: T.(result: ParseResult) -> Unit,
    ) {
        val result = parse(args, ignoreUnknownOptions, useDefaultSubcommandIfNone)
        onParse(argRegistry, result)
    }

    /**
     * Calls the validation function on each of the parsed arguments
     */
    private fun validateAll(registry: ArgRegistry): ParseResult.Error? {
        for (arg in registry) {
            if (!arg.isParsed) continue
            else {
                try {
                    val isValid = arg.validate()
                    if (!isValid) return ParseResult.Error(IllegalStateException("Argument $arg has invalid value: ${arg.value}"))
                } catch (e: Exception) {
                    return ParseResult.Error(IllegalStateException("Argument ${arg.name} has invalid value: ${arg.value}"))
                }
            }
        }
        return null
    }

    /**
     * Parses the positional arguments from the input
     * @param positionals list of registered positional arguments
     * @param mutableArgs mutable list of command-line arguments that are passed throughout other parse functions
     * @param ignoreUnknownOptions if false, throws `TooManyPositionalArgumentsException`,
     * if the count of positional arguments is bigger than the count of the registered postional arguments. If true, just ignores them.
     * @throws TooManyPositionalArgumentsException
     */

    @Throws(TooManyPositionalArgumentsException::class)
    private fun parsePositionals(
        positionals: List<NullablePositional<*>>,
        mutableArgs: MutableList<String>,
        ignoreUnknownOptions: Boolean,
    ): ParseResult.Error? {
        if (mutableArgs.size > positionals.size &&
            !ignoreUnknownOptions
        ) {
            return ParseResult.Error(TooManyPositionalArgumentsException(mutableArgs))
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
        if (nonInitialized.isNotEmpty()) return ParseResult.Error(
            IllegalStateException(
                "No value passed for positional parameters: " + nonInitialized.joinToString(
                    ", "
                ) { it.name })
        )
        return null
    }


    /**
     * Parses options from the command-line input
     * @param options list of registered options
     * @param mutableArgs mutable list of command-line arguments passed throughout parsing functions
     * @throws UnknownTokenException
     */
    private fun parseOptions(
        options: List<NullableOption<*>>,
        mutableArgs: MutableList<String>,
    ): ParseResult.Error? {
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
            if (option.required && !option.isParsed) return ParseResult.Error(IllegalStateException("Couldn't find required option ${option.name}"))
        }
        return null
    }

    /**
     * Parses flags from the command-line arguments
     * @param flags list of registered flags
     * @param mutableArgs mutable list of command-line arguments that is passed throughout parsing functions
     */
    private fun parseFlags(
        flags: List<Flag>,
        mutableArgs: MutableList<String>,
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
