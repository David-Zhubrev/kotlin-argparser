package com.appdav.argparser.registries

import com.appdav.argparser.argument.flags.Flag
import com.appdav.argparser.argument.flags.FlagBaseInternal
import com.appdav.argparser.argument.flags.InvertedFlag
import com.appdav.argparser.argument.options.NullableOption
import com.appdav.argparser.argument.positional.NullablePositional
import java.util.*

/**
 * Subcommand is a good way to provide multiple modes for your application.
 * It is a convenient way to create a complex cli for multimodal application. It is a great way to provide separate set of arguments for each
 * mode.
 * Subcommand provides DSL for registering all types of arguments, mutually exclusive groups and subcommands, meaning that subcommand can be nested.
 * @constructor
 * @param name - name of `this` Subcommand, which is used to call the specified Subcommand from the cli. The first argument from the command-line denotes the Subcommand
 * @param description - description of `this` Subcommand
 * @param useDefaultSubcommandIfNone - flag that marks that subcommand to use itself as a registry if no nested subcommand (if any registered) are called via the input array. If false and any subcommands are registered, will throw an exception
 * @see RegistryBase
 * @see OptionRegistryScope
 * @see FlagRegistryScope
 * @see PositionalRegistryScope
 * @see MutuallyExclusiveGroupsRegistryScope
 * @see SubcommandRegistryScope
 *
 */
abstract class Subcommand(
    val name: String,
    val description: String = name
        .replace("-", " ")
        .trim()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
    final override val useDefaultSubcommandIfNone: Boolean = false,
) : RegistryBase(),
    OptionRegistryScope,
    FlagRegistryScope,
    PositionalRegistryScope,
    MutuallyExclusiveGroupsRegistryScope,
    SubcommandRegistryScope {

    override fun flags(): List<FlagBaseInternal> =
        filterIsInstance<FlagBaseInternal>()

    override fun options(): List<NullableOption<*>> =
        filterIsInstance<NullableOption<*>>()

    override fun positionals(): List<NullablePositional<*>> =
        filterIsInstance<NullablePositional<*>>()

    override fun registerFlag(flag: Flag): Flag =
        registerArgument(flag)

    override fun registerInvertedFlag(invertedFlag: InvertedFlag): InvertedFlag =
        registerArgument(invertedFlag)

    override fun <E, T : NullableOption<E>> registerOption(option: T): T =
        registerArgument(option)

    override fun <E, T : NullablePositional<E>> registerPositional(positional: T): T =
        registerArgument(positional)


    private val mMutuallyExclusiveGroups = mutableListOf<MutuallyExclusiveGroup>()

    override fun mutuallyExclusiveGroups(): List<MutuallyExclusiveGroup> =
        mMutuallyExclusiveGroups

    override fun <T : MutuallyExclusiveGroup> registerMutuallyExclusiveGroup(mutuallyExclusiveGroup: T): T {
        mMutuallyExclusiveGroups.add(mutuallyExclusiveGroup)
        return mutuallyExclusiveGroup
    }

    private val mSubcommands = mutableListOf<Subcommand>()

    override fun subcommands(): List<Subcommand> = mSubcommands

    override fun <T : Subcommand> registerSubcommand(subcommand: T): T {
        mSubcommands.add(subcommand)
        return subcommand
    }

    private var mActiveSubcommand: Subcommand? = null

    override fun setActiveSubcommand(subcommand: Subcommand) {
        mActiveSubcommand = subcommand
    }

    override val activeSubcommand: Subcommand?
        get() = mActiveSubcommand
}
