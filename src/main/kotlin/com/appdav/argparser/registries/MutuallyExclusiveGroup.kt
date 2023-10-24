package com.appdav.argparser.registries

import com.appdav.argparser.argument.ArgumentBaseInternal
import com.appdav.argparser.argument.options.RequiredOption

abstract class MutuallyExclusiveGroup(
    internal val isRequired: Boolean = false,
) : RegistryBase(),
    MutuallyExclusiveGroupScope {

    var initializedArgument: ArgumentBaseInternal<*>? = null
        internal set

    final override fun options(): List<RequiredOption<*>> {
        return map { it as RequiredOption<*> }
    }

    final override fun <T : Any> registerOneOf(option: RequiredOption<T>): RequiredOption<T> =
        registerArgument(option)


    //    override fun flags(): List<Flag> =
//        filterIsInstance<Flag>()
//
//    override fun registerFlag(flag: Flag): Flag =
//        registerArgument(flag)
//
//    override fun options(): List<NullableOption<*>> =
//        filterIsInstance<NullableOption<*>>()
//
//    override fun <E, T : NullableOption<E>> registerOption(option: T): T =
//        registerArgument(option)
}
