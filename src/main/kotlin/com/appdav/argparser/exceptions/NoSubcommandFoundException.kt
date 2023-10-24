package com.appdav.argparser.exceptions

import com.appdav.argparser.registries.Subcommand

class NoSubcommandFoundException(firstArg: String) :
    IllegalArgumentException("No subcommand recognized by the keyword $firstArg")

class NoSubcommandChainFoundException(previousSubcommands: List<Subcommand>, nextArg: String):
        IllegalArgumentException("Cannot recognize subcommands: ${previousSubcommands.joinToString(" "){it.name}} $nextArg")
