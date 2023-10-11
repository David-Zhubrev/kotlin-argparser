package com.appdav.argparser

import com.appdav.argparser.argument.ArgumentBaseInternal
import com.appdav.argparser.argument.flags.Flag
import com.appdav.argparser.argument.options.NullableOption
import com.appdav.argparser.argument.positional.NullablePositional

/**
 * ArgRegistry is a collection of Arguments that is passed into ArgParser instance and iterated during parsing process. It implements the List interface, so it can be iterated through.
 * Basically, user wants to create an anonymous object inheriting ArgRegistry and use the provided DSL to register any arguments he wants to parse.
 * It provides some extensions to register arguments in a DSL way, although it is possible to register any instance of argument
 * It also provides a way to add subcommands, which are treated specially by the ArgParser
 * @see ArgumentBaseInternal
 * @see Subcommand
 */
abstract class ArgRegistry private constructor(private val list: MutableList<ArgumentBaseInternal<*>>) :
    List<ArgumentBaseInternal<*>> by list {

    constructor() : this(mutableListOf())

    /**
     * Private mutable list of subcommands registered within `this` registry
     */
    private val mSubcommands = mutableListOf<Subcommand>()


    internal fun flags(): List<Flag> =
        filterIsInstance<Flag>()

    internal fun options(): List<NullableOption<*>> =
        filterIsInstance<NullableOption<*>>()

    //TODO: add docs
    internal fun positionals(): List<NullablePositional<*>> =
        filterIsInstance<NullablePositional<*>>().sortedBy { it.position }

    /**
     * List of registered subcommands
     * @see Subcommand
     */
    val subcommands: List<Subcommand> get() = mSubcommands

    /**
     * Adds subcommand into `this` registry and returns it
     * @param subcommand instance of Subcommand to register
     * @return registered instance of Subcommand
     * @see Subcommand
     */
    fun <T : Subcommand> addSubcommand(subcommand: T): T {
        mSubcommands.add(subcommand)
        return subcommand
    }


    /**
     * Registers provided argument withing `this` registry and returns it. Argument should be registered withing `this` registry in order to make ArgParser handle it
     * @param argument instance of argument to register
     * @return provided argument
     */
    fun <E, T : ArgumentBaseInternal<E>> registerArgument(argument: T): T {
        list += argument
        return argument
    }

    /**
     * Unregisters provided argument
     * @param argument argument instance to unregister
     */
    fun unregisterArgument(argument: ArgumentBaseInternal<*>) {
        list -= argument
    }

    /**
     * Returns active subcommand after parsing, null if no subcommand is found or if you access it BEFORE parsing
     */
    var activeSubcommand: Subcommand? = null
    private set

    /**
     * Sets active subcommand
     * @param currentSubcommand Subcommand to be set as active
     */
    internal fun setActiveSubcommand(currentSubcommand: Subcommand) {
        activeSubcommand = currentSubcommand
    }


}
