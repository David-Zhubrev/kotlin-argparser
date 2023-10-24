package com.appdav.argparser.registries

import com.appdav.argparser.argument.flags.Flag
import com.appdav.argparser.argument.options.NullableOption
import com.appdav.argparser.argument.positional.NullablePositional

abstract class ArgRegistry(
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
        registerPositional(positional)


    private val mMutuallyExclusiveGroups = mutableListOf<MutuallyExclusiveGroup>()

    override fun mutuallyExclusiveGroups(): List<MutuallyExclusiveGroup> =
        mMutuallyExclusiveGroups

    override fun <T : MutuallyExclusiveGroup> addMutuallyExclusiveGroup(mutuallyExclusiveGroup: T): T {
        mMutuallyExclusiveGroups.add(mutuallyExclusiveGroup)
        return mutuallyExclusiveGroup
    }

    private val mSubcommands = mutableListOf<Subcommand>()
    private var mActiveSubcommand: Subcommand? = null

    override fun subcommands(): List<Subcommand> = mSubcommands

    override fun <T : Subcommand> registerSubcommand(subcommand: T): T {
        mSubcommands.add(subcommand)
        return subcommand
    }

    override val activeSubcommand: Subcommand?
        get() = mActiveSubcommand

    override fun setActiveSubcommand(subcommand: Subcommand) {
        mActiveSubcommand = subcommand
    }
}
