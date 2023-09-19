package com.appdav.argparser

import com.appdav.argparser.argument.ArgumentBaseInternal


abstract class ArgRegistry private constructor(private val list: MutableList<ArgumentBaseInternal<*>>) :
    List<ArgumentBaseInternal<*>> by list {

    constructor() : this(mutableListOf())


    private val mSubcommands = mutableListOf<Subcommand>()
    val subcommands: List<Subcommand> get() = mSubcommands

    fun <T : Subcommand> addSubcommand(subcommand: T): T {
        mSubcommands.add(subcommand)
        return subcommand
    }

    fun <E, T : ArgumentBaseInternal<E>> registerArgument(argument: T): T {
        list += argument
        return argument
    }

    fun unregisterArgument(argument: ArgumentBaseInternal<*>) {
        list -= argument
    }

    var activeSubcommand: Subcommand? = null
    private set

    fun setActiveSubcommand(currentSubcommand: Subcommand) {
        activeSubcommand = currentSubcommand
    }


}
