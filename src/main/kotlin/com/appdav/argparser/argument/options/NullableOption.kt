package com.appdav.argparser.argument.options

import com.appdav.argparser.argument.ArgumentBase
import java.util.*

abstract class NullableOption<T : Any> : ArgumentBase<T>() {
    abstract val token: String

    open val additionalTokens: List<String> = emptyList()

    override val name: String
        get() = token.removePrefix("--").removePrefix("-").replace("-", " ")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    override val required: Boolean = false

    internal fun allTokens(): List<String> = additionalTokens + token

}
