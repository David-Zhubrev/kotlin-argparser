package com.appdav.argparser.exceptions

class NoSubcommandFoundException(firstArg: String) :
    IllegalArgumentException("No subcommand recognized by the keyword $firstArg")
