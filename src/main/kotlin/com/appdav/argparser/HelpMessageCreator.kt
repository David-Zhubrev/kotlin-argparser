package com.appdav.argparser

import com.appdav.argparser.argument.DefaultValueArgument
import com.appdav.argparser.registries.*

object HelpMessageCreator {

    //On root registry empty args
    fun emptyArgsMessage(
        appName: String,
        helpCommand: String,
    ): String {
        return "Use $appName $helpCommand to get information about usage"
    }

    //On root registry help command
    fun createRootHelpMessage(appName: String, helpCommand: String, registry: RegistryBase): String {
        val sb = StringBuilder()
        if (registry is SubcommandRegistryScope) {
            val subcommands = registry.subcommands()
            if (subcommands.isNotEmpty()) {
                sb.append("Usage: $appName ")
                if (registry.useDefaultSubcommandIfNone) {
                    sb.append("[subcommand]")
                } else {
                    sb.append("subcommand")
                }
                sb.append(" args\n")
                sb.append("List of available subcommands:\n")
                for (sub in subcommands) {
                    sb.append("${sub.name} - ${sub.description}\n")
                }
                sb.append("\n")
                if (registry.useDefaultSubcommandIfNone) {
                    sb.append("Usage (no subcommands):\n")
                    sb.append(createRegistryArgList(registry))
                    sb.append("\nFor more info about specific subcommand use: $appName subcommand $helpCommand")
                    return sb.toString()
                }
                sb.append("For more info about specific subcommand use: $appName subcommand $helpCommand")
                return sb.toString()
            }
        }
        sb.append("Usage: $appName")
        val flags = if (registry is FlagRegistryScope)
            registry.flags() else emptyList()
        val options = if (registry is OptionRegistryScope)
            registry.options() else emptyList()
        val positionals = if (registry is PositionalRegistryScope)
            registry.positionals() else emptyList()

        //TODO: get rid of the duplicate
        if (flags.isNotEmpty()) sb.append(" [flags]")
        if (options.isNotEmpty()) sb.append(" [options]")
        if (positionals.isNotEmpty())
            sb.append(" ${positionals.joinToString(" ") { it.name.replace(" ", "-") }}")
        sb.append("\nList of available arguments:\n")
            .append(createRegistryArgList(registry))
        return sb.toString()
    }

    private fun createRegistryArgList(registry: RegistryBase): String {
        val sb = StringBuilder()
        if (registry is PositionalRegistryScope) {
            for (positional in registry.positionals()) {
                sb.append("${positional.name.replace(" ", "-")} - ${positional.description}")
                if (positional is DefaultValueArgument<*>) {
                    sb.append(", default value: ${positional.defaultValueString}\n")
                }
                sb.append("\n")
            }
        }
        if (registry is FlagRegistryScope) {
            val flags = registry.flags()
            if (flags.isNotEmpty()) {
                sb.append("\nFlags:\n")
                flags.forEach {
                    sb.append("[")
                        .append(it.allTokens().joinToString(", "))
                        .append("]")
                        .append(" ${it.name} - ${it.description}\n")
                }
            }
        }
        if (registry is OptionRegistryScope) {
            val options = registry.options()
            if (options.isNotEmpty()) {
                sb.append("\nOptions:\n")
                options.forEach {
                    if (it.required) sb.append("* ")
                    sb.append("${it.allTokens().joinToString(", ", "[", "]")} ${it.name} - ${it.description}")
                    if (it is DefaultValueArgument<*>) {
                        sb.append(", default value: ${it.defaultValueString}")
                    }
                    sb.append("\n")
                }
            }
        }
        if (registry is MutuallyExclusiveGroupsRegistryScope) {
            val exclusiveGroups = registry.mutuallyExclusiveGroups()
            if (exclusiveGroups.isNotEmpty()) {
                for (group in exclusiveGroups) {
                    sb.append("\nMutually exclusive arguments ")
                    if (group.isRequired)
                        sb.append("(only 1 from the group):\n")
                    else sb.append("(one or none from the group):\n")
                    for (arg in group.exclusiveArguments()){
                        sb.append("\t").append("${arg.allTokens().joinToString(", ", "[", "]")} ${arg.name} - ${arg.description}\n")
                    }
                }
            }
        }
        return sb.toString()
    }

    //Called when subcommand1 subcommand2 ... help is the input
    fun createSubcommandSequenceHelpMessage(
        appName: String,
        helpCommand: String,
        subcommand: Subcommand,
        previousSubcommands: List<Subcommand>,
    ): String {
        val sb = StringBuilder()
        sb.append("Usage: $appName")
        if (previousSubcommands.isNotEmpty()) {
            sb.append(" ").append(previousSubcommands.joinToString(" ") { it.name })
        }
        sb.append(" ${subcommand.name}")

//            .append(previousSubcommands.dropLast(1).joinToString(" ") { it.name })
//            .append(" ${subcommand.name}")
        val subcommands = subcommand.subcommands()
        if (subcommands.isNotEmpty()) {
            if (subcommand.useDefaultSubcommandIfNone) {
                sb.append(" [subcommand]")
            } else {
                sb.append(" subcommand")
            }
            if (subcommand.positionals().isNotEmpty()) {
                sb.append(" ${subcommand.positionals().joinToString(" ") { it.name }}")
            }

//            sb.append(" args\n")
            sb.append("\nList of available subcommands:\n")
            for (sub in subcommands) {
                sb.append("${sub.name} - ${sub.description}\n")
            }
            if (subcommand.useDefaultSubcommandIfNone) {
                sb.append("Usage (no subcommands):\n")
                sb.append(createRegistryArgList(subcommand))
            }
            sb.append(
                "\nFor more info about specific subcommand use: $appName ${
                    previousSubcommands.joinToString(" ") { it.name }
                } ${subcommand.name} subcommand $helpCommand"
            )
            return sb.toString()
        } else {
            val flags = subcommand.flags()
            val options = subcommand.options()
            val positionals = subcommand.positionals()
            if (flags.isNotEmpty()) sb.append(" [flags]")
            if (options.isNotEmpty()) sb.append(" [options]")
            if (positionals.isNotEmpty())
                sb.append(" ${positionals.joinToString(" ") { it.name }}\n")
            sb.append("\nList of available arguments:\n")
                .append(createRegistryArgList(subcommand))
            return sb.toString()
        }


    }
}
