package com.appdav.argparser

sealed class ParseResult {

    data object Success: ParseResult()
    data class EmptyArgs(val defaultHelpMessage: String): ParseResult()

    data class Error(val exception: Throwable): ParseResult()

    data class HelpCommand(val defaultHelpMessage: String): ParseResult()

}
