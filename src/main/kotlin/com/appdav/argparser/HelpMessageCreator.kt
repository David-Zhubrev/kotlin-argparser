package com.appdav.argparser

import com.appdav.argparser.registries.RegistryBase
import com.appdav.argparser.registries.Subcommand

class HelpMessageCreator(
    private val appName: String,
    private val registry: RegistryBase,
) {
    fun createMessageForNoArgs(): String {
        return "Use $appName --help to get more information on usage"
    }

    fun createDefaultHelpMessage(useDefaultSubcommandIfNone: Boolean): String {
        return if (registry.subcommands.isNotEmpty()) {
            if (useDefaultSubcommandIfNone) {
                createHelpMessageForSubcommandsAndDefault()
            } else {
                createHelpMessageForSubcommandsAndNoDefault()
            }
        } else {
            createArgumentListHelpMessage(registry, true)
        }
    }

    private fun createArgumentListHelpMessage(registry: RegistryBase, addTitle: Boolean): String {
        val sb = StringBuilder()
        if (addTitle) {
            val hasOptions = registry.options().isNotEmpty()
            val hasFlags = registry.flags().isNotEmpty()
            val positionals = registry.positionals()
            with(sb) {
                append("Usage: $appName")
                if (hasOptions) append(" [options]")
                if (hasFlags) append(" [flags]")
                if (positionals.isNotEmpty()) {
                    append(" ")
                    append(positionals.joinToString(" ") { it.name.replace(" ", "-") })
                }
                append("\n")
            }
        }
        registry.positionals().forEach {
            sb.append("Positional arguments: \n")
            sb.append("${if (it.required) "*" else ""}${it.name} - ${it.description}\n")
        }
        with(registry.options()) {
            if (isNotEmpty()) {
                sb.append("Options:\n")
                forEach {
                    sb.append(
                        "[${if (it.required) "*" else ""}${
                            it.allTokens().joinToString(", ")
                        }] ${it.name} - ${it.description}\n"
                    )
                }
            }
        }
        with(registry.flags()) {
            if (isNotEmpty()) {
                sb.append("Flags:\n")
                forEach {
                    sb.append("[${it.allTokens().joinToString(", ")}] ${it.name} - ${it.description}\n")
                }
            }
        }
        return sb.toString()
    }


    private fun createHelpMessageForSubcommandsAndNoDefault(): String {
        val sb = StringBuilder()
        sb.append("Usage: $appName [subcommand] args\n")
            .append("List of available subcommands:\n")
        for (subcommand in registry.subcommands) {
            sb.append("${subcommand.name} - ${subcommand.description}\n")
        }
        sb.append("default (no subcommand) - $appName default behaviour\n")
            .append("For more info on specific subcommand type:\n$appName subcommand help\n")
        return sb.toString()
    }

    private fun createHelpMessageForSubcommandsAndDefault(): String {
        val sb = StringBuilder()
        sb.append(createHelpMessageForSubcommandsAndNoDefault())

        val argListMessage = createArgumentListHelpMessage(registry, false)
        if (argListMessage.isNotBlank()) {
            sb.append("Default mode:\n")
                .append(argListMessage)
        }
        return sb.toString()
    }

    fun createSubcommandHelpMessage(subcommand: Subcommand): String {
        val sb = StringBuilder()
        sb.append("Usage: $appName ${subcommand.name }")
        if (subcommand.options().isNotEmpty()) sb.append(" [options]")
        if (subcommand.flags().isNotEmpty()) sb.append(" [flags]")
        val positionals = subcommand.positionals()
        if (positionals.isNotEmpty())
            sb.append(" ")
                .append(positionals.joinToString(" ") { it.name.replace(" ", "-") })
        sb.append("\n")
        sb.append(createArgumentListHelpMessage(subcommand, false))
        return sb.toString()
    }

}
