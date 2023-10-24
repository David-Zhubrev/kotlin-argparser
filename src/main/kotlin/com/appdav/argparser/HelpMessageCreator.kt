package com.appdav.argparser
//
//import com.appdav.argparser.registries.*
//
//class HelpMessageCreator(
//    private val appName: String,
//    private val registry: ArgRegistry,
//) {
//    fun emptyArgsMessage(): String {
//        return "Use $appName --help to get more information on usage"
//    }
//
//    fun createRootHelpMessage(useDefaultSubcommandIfNone: Boolean): String {
//        return if (registry.subcommands().isNotEmpty()) {
//            if (useDefaultSubcommandIfNone) {
//                createHelpMessageForSubcommandsAndDefault()
//            } else {
//                createHelpMessageForSubcommandsAndNoDefault()
//            }
//        } else {
//            createArgumentListHelpMessage(registry, true)
//        }
//    }
//
//    private fun createArgumentListHelpMessage(registry: RegistryBase, addTitle: Boolean): String {
//        val sb = StringBuilder()
//        val options = if (registry is OptionRegistry) registry.options() else emptyList()
//        val flags =  if (registry is FlagRegistry) registry.flags() else emptyList()
////            val hasOptions = registry is OptionRegistry && registry.options().isNotEmpty()
////            val hasFlags = registry is FlagRegistry && registry.flags().isNotEmpty()
//        val positionals = if (registry is PositionalRegistry) registry.positionals() else emptyList()
//        if (addTitle) {
//            with(sb) {
//                append("Usage: $appName")
//                if (options.isNotEmpty()) append(" [options]")
//                if (flags.isNotEmpty()) append(" [flags]")
//                if (positionals.isNotEmpty()) {
//                    append(" ")
//                    append(positionals.joinToString(" ") { it.name.replace(" ", "-") })
//                }
//                append("\n")
//            }
//        }
//        positionals.forEach {
//            sb.append("Positional arguments: \n")
//            sb.append("${if (it.required) "*" else ""}${it.name} - ${it.description}\n")
//        }
//        with(options) {
//            if (isNotEmpty()) {
//                sb.append("Options:\n")
//                forEach {
//                    sb.append(
//                        "[${if (it.required) "*" else ""}${
//                            it.allTokens().joinToString(", ")
//                        }] ${it.name} - ${it.description}\n"
//                    )
//                }
//            }
//        }
//        with(flags) {
//            if (isNotEmpty()) {
//                sb.append("Flags:\n")
//                forEach {
//                    sb.append("[${it.allTokens().joinToString(", ")}] ${it.name} - ${it.description}\n")
//                }
//            }
//        }
//        return sb.toString()
//    }
//
//
//    private fun createHelpMessageForSubcommandsAndNoDefault(): String {
//        val sb = StringBuilder()
//        sb.append("Usage: $appName [subcommand] args\n")
//            .append("List of available subcommands:\n")
//        for (subcommand in registry.subcommands()) {
//            sb.append("${subcommand.name} - ${subcommand.description}\n")
//        }
//        sb.append("default (no subcommand) - $appName default behaviour\n")
//            .append("For more info on specific subcommand type:\n$appName subcommand help\n")
//        return sb.toString()
//    }
//
//    private fun createHelpMessageForSubcommandsAndDefault(): String {
//        val sb = StringBuilder()
//        sb.append(createHelpMessageForSubcommandsAndNoDefault())
//
//        val argListMessage = createArgumentListHelpMessage(registry, false)
//        if (argListMessage.isNotBlank()) {
//            sb.append("Default mode:\n")
//                .append(argListMessage)
//        }
//        return sb.toString()
//    }
//
//    fun createSubcommandHelpMessage(subcommand: Subcommand): String {
//        val sb = StringBuilder()
//        sb.append("Usage: $appName ${subcommand.name }")
//        if (subcommand.options().isNotEmpty()) sb.append(" [options]")
//        if (subcommand.flags().isNotEmpty()) sb.append(" [flags]")
//        val positionals = subcommand.positionals()
//        if (positionals.isNotEmpty())
//            sb.append(" ")
//                .append(positionals.joinToString(" ") { it.name.replace(" ", "-") })
//        sb.append("\n")
//        sb.append(createArgumentListHelpMessage(subcommand, false))
//        return sb.toString()
//    }
//
//    fun createSubcommandSequenceHelpMessage(subcommandsSequence: MutableList<Subcommand>): String {
//
//    }
//
//}
