package com.appdav.argparser.registries

import com.appdav.argparser.argument.flags.Flag
import com.appdav.argparser.argument.options.NullableOption
import com.appdav.argparser.argument.options.RequiredOption
import com.appdav.argparser.argument.positional.NullablePositional

interface FlagRegistryScope {

    fun flags(): List<Flag>

    fun registerFlag(flag: Flag): Flag

}


interface OptionRegistryScope {
    fun options(): List<NullableOption<*>>

    fun <E, T : NullableOption<E>> registerOption(option: T): T

}

interface PositionalRegistryScope {

    fun positionals(): List<NullablePositional<*>>

    fun <E, T : NullablePositional<E>> registerPositional(positional: T): T
}

interface SubcommandRegistryScope {
    fun subcommands(): List<Subcommand>
    fun <T : Subcommand> registerSubcommand(subcommand: T): T

    fun setActiveSubcommand(subcommand: Subcommand)

    val activeSubcommand: Subcommand?

    val useDefaultSubcommandIfNone: Boolean

}

interface MutuallyExclusiveGroupsRegistryScope{
    fun mutuallyExclusiveGroups(): List<MutuallyExclusiveGroup>

    fun <T: MutuallyExclusiveGroup> addMutuallyExclusiveGroup(mutuallyExclusiveGroup: T): T



}

interface MutuallyExclusiveGroupScope{

    fun <T: Any> registerOneOf(option: RequiredOption<T>): RequiredOption<T>
    fun options(): List<RequiredOption<*>>

}
