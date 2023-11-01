package com.appdav.argparser.exceptions

import com.appdav.argparser.registries.Subcommand

///***
// * This exception occurs when provided subcommand is not registered inside parsed registry
// */
//class NoSubcommandFoundException(firstArg: String) :
//    IllegalArgumentException("No subcommand recognized by the keyword $firstArg")

/**
 * This exception occurs when provided subcommand chain is not registered inside parsed registry
 */
class NoSubcommandChainFoundException(previousSubcommands: List<Subcommand>, nextArg: String):
        IllegalArgumentException("Cannot recognize subcommands: ${previousSubcommands.joinToString(" "){it.name}} $nextArg")
