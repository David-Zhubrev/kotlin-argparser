package com.appdav.argparser.registries

import com.appdav.argparser.argument.ExclusiveArgument
import com.appdav.argparser.argument.flags.Flag
import com.appdav.argparser.argument.flags.FlagBaseInternal
import com.appdav.argparser.argument.flags.InvertedFlag
import com.appdav.argparser.argument.positional.NullablePositional

/**
 * Interface that marks a registry that can provide a DSL for registering flags
 * @see com.appdav.argparser.argument.flags.Flag
 */
interface FlagRegistryScope {

    /**
     * Provides registered flags
     * @return list of registered Flag arguments
     * @see Flag
     */
    fun flags(): List<FlagBaseInternal>

    /**
     * Register provided Flag instance
     * @param flag Flag instance
     * @return provided Flag instance (convenience return)
     * @see Flag
     */
    fun registerFlag(flag: Flag): Flag

    /**
     * Registers provided InvertedFlag instance
     * @param invertedFlag InvertedFlag instance
     * @return provided InvertedFlag instance(convenience return)
     * @see InvertedFlag
     */
    fun registerInvertedFlag(invertedFlag: InvertedFlag): InvertedFlag

}

/**
 * Interface that marks a registry that can provide a DSL for registering positional arguments
 * @see com.appdav.argparser.argument.positional.PositionalsProvider
 * @see com.appdav.argparser.argument.positional.NullablePositional
 * @see com.appdav.argparser.argument.positional.RequiredPositional
 * @see com.appdav.argparser.argument.positional.PositionalWithDefaultValue
 */

interface PositionalRegistryScope {

    /**
     * Provides registered positional arguments
     * @return list of registered positional arguments
     * @see NullablePositional
     */
    fun positionals(): List<NullablePositional<*>>

    /**
     * Register provided positional argument and returns it
     * @param positional Positional argument instance
     * @return provided positional argument instance
     */
    fun <E, T : NullablePositional<E>> registerPositional(positional: T): T

}

/**
 * Interface that marks a registry that can provide a DSL for registering subcommands
 * @see Subcommand
 * @see SubcommandRegistryScope.registerSubcommand
 */

interface SubcommandRegistryScope {

    /**
     * @return list of registered subcommands
     */
    fun subcommands(): List<Subcommand>

    /**
     * Registers an instance of subcommand and returns it
     * @param subcommand instance
     * @return provided subcommand instance
     */
    fun <T : Subcommand> registerSubcommand(subcommand: T): T

    /**
     * Sets provided subcommand instance as currently active inside `this` registry
     * @param subcommand active subcommand
     */
    fun setActiveSubcommand(subcommand: Subcommand)

    /**
     * Get currently active subcommand. Can be null if no active subcommand found
     */
    val activeSubcommand: Subcommand?

    /**
     * When true, uses `this` registry as a default, meaning that if no subcommand found, `this` registry is used.
     * If false, throws an exception if no subcommand found
     */
    val useDefaultSubcommandIfNone: Boolean

}

/**
 * Interface that marks a registry that can provide a DSL for registering MutuallyExclusiveGroups
 * @see MutuallyExclusiveGroup
 * @see MutuallyExclusiveGroupsRegistryScope.registerMutuallyExclusiveGroup
 */
interface MutuallyExclusiveGroupsRegistryScope {

    /**
     * @return list of registered MutuallyExclusiveGroups
     * @see MutuallyExclusiveGroup
     */
    fun mutuallyExclusiveGroups(): List<MutuallyExclusiveGroup>

    /**
     * Registers provided MutuallyExclusiveGroup instance and returns it
     * @param mutuallyExclusiveGroup MutuallyExclusiveGroup instance to register
     * @return provided MutuallyExclusiveGroup
     * @see MutuallyExclusiveGroup
     */
    fun <T : MutuallyExclusiveGroup> registerMutuallyExclusiveGroup(mutuallyExclusiveGroup: T): T


}

/**
 * Interface that marks a MutuallyExclusiveGroup registry that can provide a DSL for registering ExclusiveArguments
 * @see MutuallyExclusiveGroup
 * @see ExclusiveArgument
 */
interface MutuallyExclusiveGroupScope {

    /**
     * Registers an instance of ExclusiveArgument and returns it
     * @param exclusiveArgument ExclusiveArgument instance to register
     * @return provided ExclusiveArgument instance
     */
    fun <T : Any> registerExclusive(exclusiveArgument: ExclusiveArgument<T>): ExclusiveArgument<T>

    /**
     * @return list of registered ExclusiveArgument
     */
    fun exclusiveArguments(): List<ExclusiveArgument<*>>

}
