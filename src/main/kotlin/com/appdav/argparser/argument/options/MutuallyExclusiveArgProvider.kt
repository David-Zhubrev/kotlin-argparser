package com.appdav.argparser.argument.options

import com.appdav.argparser.argument.ArgumentProviderDsl
import com.appdav.argparser.argument.ExclusiveArgument
import com.appdav.argparser.converter.ValueConverter
import com.appdav.argparser.registries.MutuallyExclusiveGroupScope
import java.util.*

/**
 * Creates, registers and returns an instance of ExclusiveArgument
 * @see ExclusiveArgument
 * @param token a token associated with created ExclusiveArgument
 * @param converter ValueConverter that will be used to convert string input into ExclusiveArgument's value
 * @param additionalTokens additional tokens associated with created ExclusiveArgument
 * @param name name for created option
 * @param description description
 * @param validator validation function for the created parameter. returns true by default
 * @return newly created and registered instance of ExclusiveArgument
 * @see ExclusiveArgument
 * @see ValueConverter
 * @see com.appdav.argparser.registries.MutuallyExclusiveGroup
 */
@ArgumentProviderDsl
fun <T : Any> MutuallyExclusiveGroupScope.exclusiveArgument(
    token: String,
    converter: ValueConverter<T>,
    additionalTokens: List<String> = emptyList(),
    name: String = token.removePrefix("--").removePrefix("-").replace("-", " ")
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
    description: String = name,
    validator: (T?) -> Boolean = { true },
): ExclusiveArgument<T> {
    val arg = object : ExclusiveArgument<T>() {
        override val name: String = name
        override val converter: ValueConverter<T> = converter
        override val token: String = token
        override val additionalTokens: List<String> = additionalTokens
        override val description: String = description
        override val validator: (T?) -> Boolean = validator
    }
    registerExclusive(arg)
    return arg
}
