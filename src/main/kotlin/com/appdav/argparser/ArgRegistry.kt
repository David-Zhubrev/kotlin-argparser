package com.appdav.argparser

import com.appdav.argparser.argument.ArgumentBase


abstract class ArgRegistry private constructor(private val list: MutableList<ArgumentBase<*>>) :
    List<ArgumentBase<*>> by list {

    constructor() : this(mutableListOf())


    private val mSubcommands = mutableListOf<Subcommand>()
    val subcommands: List<Subcommand> get() = mSubcommands

    fun <T : Subcommand> addSubcommand(subcommand: T): T {
        mSubcommands.add(subcommand)
        return subcommand
    }

    fun <E, T : ArgumentBase<E>> registerArgument(argument: T): T {
        list += argument
        return argument
    }

    fun unregisterArgument(argument: ArgumentBase<*>) {
        list -= argument
    }


}
