package com.appdav.argparser.registries

import com.appdav.argparser.argument.flags.Flag
import com.appdav.argparser.argument.options.NullableOption
import com.appdav.argparser.argument.positional.NullablePositional
import java.util.*

/**
 * Subcommand is a good way to provide multiple modes for your application.
 * It is a convenient way to create a complex cli for multi-mode application. It is a great way to provide separate set of arguments for each
 * mode.
 * Subcommand is basically inherits ArgRegistry, which means it is a collection of arguments and a set of methods for a quick registration of arguments
 * @constructor name - name of `this` Subcommand, which is used to call the specified Subcommand from the cli. The first argument from the command-line denotes the Subcommand
 * @see RegistryBase
 *
 */
abstract class Subcommand(
    val name: String,
    val description: String = name
        .replace("-", " ")
        .trim()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
    final override val useDefaultSubcommandIfNone: Boolean = false
) : RegistryBase(),
    OptionRegistryScope,
    FlagRegistryScope,
    PositionalRegistryScope,
    MutuallyExclusiveGroupsRegistryScope,
    SubcommandRegistryScope {

    override fun flags(): List<Flag> =
        filterIsInstance<Flag>()

    override fun options(): List<NullableOption<*>> =
        filterIsInstance<NullableOption<*>>()

    override fun positionals(): List<NullablePositional<*>> =
        filterIsInstance<NullablePositional<*>>()

    override fun registerFlag(flag: Flag): Flag =
        registerArgument(flag)

    override fun <E, T : NullableOption<E>> registerOption(option: T): T =
        registerArgument(option)

    override fun <E, T : NullablePositional<E>> registerPositional(positional: T): T =
        registerArgument(positional)


    private val mMutuallyExclusiveGroups = mutableListOf<MutuallyExclusiveGroup>()

    override fun mutuallyExclusiveGroups(): List<MutuallyExclusiveGroup> =
        mMutuallyExclusiveGroups

    override fun <T : MutuallyExclusiveGroup> addMutuallyExclusiveGroup(mutuallyExclusiveGroup: T): T {
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
