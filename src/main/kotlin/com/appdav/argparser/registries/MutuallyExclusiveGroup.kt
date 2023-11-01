package com.appdav.argparser.registries

import com.appdav.argparser.argument.ArgumentBaseInternal
import com.appdav.argparser.argument.ExclusiveArgument

/**
 * Registry that provides a DSL for registering ExclusiveArguments.
 * Mutually exclusive group is a set of command-line arguments that can exclude each other,
 * meaning only one (or none) of them can be provided simultaneously.
 * If marked as required, one of the ExclusiveArguments has to be initialized, otherwise it's either none or one of arguments initialized.
 * @see ExclusiveArgument
 * @see RegistryBase
 * @see MutuallyExclusiveGroupScope
 */
abstract class MutuallyExclusiveGroup(
    /**
     * if marked as required, one of the registered ExclusiveArguments must be initialized, otherwise an exception will be thrown.
     * If false, then its one or none initialized ExclusiveArgument. False by default
     */
    internal val isRequired: Boolean = false,
) : RegistryBase(),
    MutuallyExclusiveGroupScope {

    /**
     * Initialized argument getter which can be used to acquire that one (or none) argument of the group that has been parsed by the ArgParser
     */
    var initializedArgument: ArgumentBaseInternal<*>? = null
        internal set

    final override fun exclusiveArguments(): List<ExclusiveArgument<*>> {
        return filterIsInstance<ExclusiveArgument<*>>()
    }

    final override fun <T : Any> registerExclusive(exclusiveArgument: ExclusiveArgument<T>): ExclusiveArgument<T> =
        registerArgument(exclusiveArgument)

}
