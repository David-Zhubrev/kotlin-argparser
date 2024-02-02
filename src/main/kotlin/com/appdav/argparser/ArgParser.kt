package com.appdav.argparser

import com.appdav.argparser.argument.ArgumentBaseInternal
import com.appdav.argparser.argument.flags.FlagBaseInternal
import com.appdav.argparser.argument.options.NullableOption
import com.appdav.argparser.argument.positional.NullablePositional
import com.appdav.argparser.exceptions.*
import com.appdav.argparser.registries.*


/**
 * Argument parser that handles the parsing process
 */
class ArgParser<T : ArgRegistry>(
    private val appName: String,
    private val argRegistry: T,
    private val helpArguments: List<String> = listOf("help", "-h", "--help"),
) {

    init {
        if (helpArguments.isEmpty()) throw IllegalArgumentException("No help arguments provided. Please provide at least one help argument for the ArgParser instance")
    }

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

    private fun String.isHelpCommand(): Boolean {
        return this in helpArguments
    }

    private lateinit var initialArguments: Array<String>

    /**
     * Parses the registered arguments and subcommands. Firstly, it tries to figure out if any of the subcommands is called.
     * After that it parses the arguments in the following order: flags, options and then the positionals. For each type of arguments it checks if some of the required arguments
     * are missing and throws the RequiredArgumentMissingException if there are some.
     * After the parsing process it calls the validation function for each of the argument.
     * @param args command-line arguments array passed into the main function
     * @param ignoreUnknownOptions if true, it ignores any unknown flags and options, which are denoted by unregistered tokens. If false, throws an
     * UnknownTokenException. False by default.
     */

    fun parse(
        args: Array<String>,
        ignoreUnknownOptions: Boolean = false,
    ): ParseResult {
        initialArguments = args
        if (args.isEmpty()) {
            return ParseResult.EmptyArgs(HelpMessageCreator.emptyArgsMessage(appName, helpArguments.first()))
        }
        if (args.first() in helpArguments)
            return ParseResult.HelpCommand(
                HelpMessageCreator.createRootHelpMessage(
                    appName,
                    helpArguments.first(),
                    argRegistry
                )
            )
        return handleSubcommands(
            argRegistry,
            args.toMutableList(),
            ignoreUnknownOptions
        )
    }

    private val subcommandsSequence = mutableListOf<Subcommand>()

    private fun handleSubcommands(
        registry: RegistryBase,
        mutableArgs: MutableList<String>,
        ignoreUnknownOptions: Boolean,
    ): ParseResult {
        if (mutableArgs.isNotEmpty()) {
            if (mutableArgs.first() in helpArguments) {
                return if (subcommandsSequence.isEmpty()) {
                    //Probably never happens
                    ParseResult.HelpCommand(
                        HelpMessageCreator.createRootHelpMessage(
                            appName, helpArguments.first(), registry
                        )
                    )
                } else {
                    ParseResult.HelpCommand(
                        HelpMessageCreator.createSubcommandSequenceHelpMessage(
                            appName,
                            helpArguments.first(),
                            registry as Subcommand,
                            subcommandsSequence.dropLast(1)
                        )
                    )
                }
            }
        }

        //Check if subcommands registered
        if (registry is SubcommandRegistryScope) {
            val subcommands = registry.subcommands()
            if (subcommands.isNotEmpty()) {
                val workingSubcommand = acquireCurrentSubcommand(subcommands, mutableArgs)
                if (workingSubcommand == null) {
                    return if (mutableArgs.isNotEmpty() && mutableArgs.first() in helpArguments) {
                        ParseResult.HelpCommand(
                            HelpMessageCreator.createSubcommandSequenceHelpMessage(
                                appName,
                                helpArguments.first(),
                                subcommands.last(),
                                subcommandsSequence.dropLast(1)
                            )
                        )
                    } else if (!registry.useDefaultSubcommandIfNone) {
                        ParseResult.Error(
                            NoSubcommandChainFoundException(
                                subcommandsSequence,
                                mutableArgs.firstOrNull() ?: ""
                            )
                        )
                    } else {
                        parseRegistryArguments(registry, mutableArgs, ignoreUnknownOptions)
                    }
                } else {
                    mutableArgs.removeFirst()
                    subcommandsSequence.add(workingSubcommand)
                    registry.setActiveSubcommand(workingSubcommand)
                    return handleSubcommands(workingSubcommand, mutableArgs, ignoreUnknownOptions)
                }
            }
        }
        return parseRegistryArguments(registry, mutableArgs, ignoreUnknownOptions)
    }

    private fun parseRegistryArguments(
        registry: RegistryBase,
        mutableArgs: MutableList<String>,
        ignoreUnknownOptions: Boolean,
    ): ParseResult {
        if (registry is MutuallyExclusiveGroupsRegistryScope) {
            val mutuallyExclusive = registry.mutuallyExclusiveGroups()
            mutuallyExclusive.forEach { parseExclusiveGroup(it, mutableArgs)?.let { error -> return error } }
        }
        //TODO: add grouped args impl

        if (registry is FlagRegistryScope) {
            parseFlags(registry.flags(), mutableArgs)?.let { error -> return error }
        }
        if (registry is OptionRegistryScope) {
            parseOptions(registry.options(), mutableArgs)?.let { error -> return error }
        }
        if (!ignoreUnknownOptions) {
            checkForUnknownOptionsAndFlags(mutableArgs)?.let { error -> return error }
        }
        if (registry is PositionalRegistryScope) {
            val positionals = registry.positionals()
            parsePositionals(positionals, mutableArgs, ignoreUnknownOptions)?.let { error -> return error }
        }
        validateAll(registry)?.let { error -> return error }
        return ParseResult.Success
    }


    private fun parseExclusiveGroup(
        mutuallyExclusiveGroup: MutuallyExclusiveGroup,
        mutableArgs: MutableList<String>,
    ): ParseResult.Error? {
        var firstParsed: Pair<ArgumentBaseInternal<*>, String>? = null
        for (arg in mutuallyExclusiveGroup.exclusiveArguments()) {

            val token = mutableArgs.find { it in arg.allTokens() }
            if (token != null) {
                if (firstParsed != null) {
                    return ParseResult.Error(MultipleExclusiveArgsException(firstParsed.second, token))
                }
                try {
                    val valueIndex: Int = mutableArgs.indexOf(token) + 1
                    if (mutableArgs.lastIndex < valueIndex) {
                        return ParseResult.Error(NoValueForOptionException(arg))
                    }
                    arg.parseInput(mutableArgs[valueIndex])
                    arg.validate()
                    mutuallyExclusiveGroup.initializedArgument = arg
                    firstParsed = arg to token
                    mutableArgs.removeAt(valueIndex)
                    mutableArgs.remove(token)
                } catch (e: ValueConversionException) {
                    return ParseResult.Error(e)
                }
            }
        }
        if (mutuallyExclusiveGroup.isRequired && firstParsed == null) {
            return ParseResult.Error(RequiredExclusiveGroupMissingException(mutuallyExclusiveGroup))
        }
        return null
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
        onParse: T.(result: ParseResult) -> Unit,
    ) {
        val result = parse(args, ignoreUnknownOptions)
        onParse(argRegistry, result)
    }

    /**
     * Calls the validation function on each of the parsed arguments
     */
    private fun validateAll(registry: RegistryBase): ParseResult.Error? {
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
                try {
                    positional.parseInput(next)
                } catch (e: Exception) {
                    return ParseResult.Error(e)
                }
                continue@outer
            } else {
                break@outer
            }
        }
        val nonInitialized = positionals.filter { !it.isParsed && it.required }
        if (nonInitialized.isNotEmpty()) return ParseResult.Error(RequiredArgumentMissingException(nonInitialized.first()))
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
            val allTokens = option.allTokens()
            val arg = mutableArgs.find { it in allTokens }
            if (arg != null) {
                val value =
                    mutableArgs.getOrNull(mutableArgs.indexOf(arg) + 1)?.takeIf { !it.startsWith("-") }
                        ?: return ParseResult.Error(NoValueForOptionException(option))
                try {
                    option.parseInput(value)
                } catch (e: Exception) {
                    return ParseResult.Error(e)
                }
                mutableArgs.remove(arg)
                mutableArgs.remove(value)
                if (mutableArgs.find { it in allTokens } != null) return ParseResult.Error(
                    ArgumentMultipleOccurrenceException(option.name, option, initialArguments)
                )
                continue@outer
            }
            if (option.required && !option.isParsed)
                return ParseResult.Error(RequiredArgumentMissingException(option))
        }
        return null
    }

    /**
     * Parses flags from the command-line arguments
     * @param flags list of registered flags
     * @param mutableArgs mutable list of command-line arguments that is passed throughout parsing functions
     */
    private fun parseFlags(
        flags: List<FlagBaseInternal>,
        mutableArgs: MutableList<String>,
    ): ParseResult.Error? {
        for (flag in flags) {
            val arg = mutableArgs.find { it in flag.allTokens() }
            if (arg != null) {
                flag.parseInput("")
                mutableArgs.remove(arg)
                if (mutableArgs.find { it in flag.allTokens() } != null) {
                    return ParseResult.Error(ArgumentMultipleOccurrenceException(flag.name, flag, initialArguments))
                }
            }
        }
        return null
    }


}
