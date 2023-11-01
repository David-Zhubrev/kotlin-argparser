package com.appdav.argparser.argument

import com.appdav.argparser.exceptions.NonInitializedValueException
import java.util.*

/**
 * ExclusiveArgument is an argument that can be used inside MutuallyExclusiveGroup.
 * It is a tokenized argument that has non-nullable value.
 */
abstract class ExclusiveArgument<T : Any> : ArgumentBaseInternal<T>(),
    TokenizedArgument {

    final override val value: T
        @Throws(NonInitializedValueException::class)
        get() = super.value ?: throw NonInitializedValueException(this)

    override val name: String
        get() = token.removePrefix("--").removePrefix("-").replace("-", " ")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    final override val required: Boolean = true

    final override fun allTokens(): List<String> = super.allTokens()


}
