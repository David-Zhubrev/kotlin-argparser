package com.appdav.argparser.registries

abstract class MutuallyExclusiveGroup(
    internal val isRequired: Boolean = false,
) : RegistryBase(){

    fun <T: Subcommand> addSubcommand(subcommand: T, builder: T.() -> Unit): T{
        return addSubcommand(subcommand.apply(builder))
    }

}
