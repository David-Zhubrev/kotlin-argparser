package com.appdav.argparser

class HelpMessageCreator(
    private val appName: String,
    private val registry: ArgRegistry,
) {
    fun createMessageForNoArgs(): String {
        return "Use $appName --help to get more information on usage"
    }

    fun createDefaultHelpMessage(useDefaultSubcommandIfNone: Boolean): String {
        return if (registry.subcommands.isNotEmpty()) {
            if (useDefaultSubcommandIfNone) {
                createHelpMessageForSubcommandsAndDefault()
            } else {
                createHelpMessageForeSubcommandsAndNoDefault()
            }
        } else {
            createArgumentListHelpMessage(registry, true)
        }
    }

    private fun createArgumentListHelpMessage(registry: ArgRegistry, addTitle: Boolean): String {
        val sb = StringBuilder()
        if (addTitle)
            sb.append("Usage: $appName [options] args\n")
        registry.positionals().forEach {
            sb.append("${if (it.required) "*" else ""}${it.name} - ${it.description}\n")
        }
        with(registry.options()){
            if (isNotEmpty()){
                sb.append("Options:\n")
                forEach {
                    sb.append("[${if (it.required) "*" else ""}${it.allTokens().joinToString(", ")}] ${it.name} - ${it.description}\n")
                }
            }
        }

        with(registry.flags()){
            if (isNotEmpty()){
                sb.append("Flags:\n")
                forEach {
                    sb.append("[${it.allTokens().joinToString(", ")}] ${it.name} - ${it.description}\n")
                }
            }
        }
        return sb.toString()
    }


    private fun createHelpMessageForeSubcommandsAndNoDefault(): String {
        val sb = StringBuilder()
        sb.append("Usage: $appName [subcommand] args\n")
            .append("List of available subcommands:\n")
        for (subcommand in registry.subcommands) {
            sb.append("${subcommand.name} - ${subcommand.description}\n")
        }
        sb.append("default (no arg) - $appName default behaviour\n")
            .append("For more info on specific subcommand type:\n$appName subcommand help\n")
        return sb.toString()
    }

    private fun createHelpMessageForSubcommandsAndDefault(): String {
        val sb = StringBuilder()
        sb.append(createHelpMessageForeSubcommandsAndNoDefault())

        val argListMessage = createArgumentListHelpMessage(registry, false)
        if (argListMessage.isNotBlank()){
            sb.append("Default mode:\n")
                .append(argListMessage)
        }
        return sb.toString()
    }

    fun createSubcommandHelpMessage(subcommand: Subcommand): String {
        return "Usage: $appName ${subcommand.name} [option] [flag] args\n" +
                createArgumentListHelpMessage(subcommand, false)
    }

}
