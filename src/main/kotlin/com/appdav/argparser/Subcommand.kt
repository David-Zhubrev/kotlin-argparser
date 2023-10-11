package com.appdav.argparser

import java.util.*

/**
 * Subcommand is a good way to provide multiple modes for your application.
 * It is a convenient way to create a complex cli for multi-mode application. It is a great way to provide separate set of arguments for each
 * mode.
 * Subcommand is basically inherits ArgRegistry, which means it is a collection of arguments and a set of methods for a quick registration of arguments
 * @constructor name - name of `this` Subcommand, which is used to call the specified Subcommand from the cli. The first argument from the command-line denotes the Subcommand
 * @see ArgRegistry
 *
 */
abstract class Subcommand(
    val name: String,
    val description: String = name
        .replace("-", " ")
        .trim()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
) : ArgRegistry()
