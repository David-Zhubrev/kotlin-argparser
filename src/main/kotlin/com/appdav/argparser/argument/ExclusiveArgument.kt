package com.appdav.argparser.argument

import java.util.*
import kotlin.reflect.KProperty

/**
 * ExclusiveArgument is an argument that can be used inside MutuallyExclusiveGroup.
 * It is a tokenized argument that has non-nullable value.
 */
abstract class ExclusiveArgument<T : Any> : ArgumentBaseInternal<T>(),
    TokenizedArgument,
    DelegateArgument<T> {

    final override val value: T?
        get() = super.value

    override val name: String
        get() = token.removePrefix("--").removePrefix("-").replace("-", " ")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    override fun getValue(thisRef: Any?, kProperty: KProperty<*>): T? {
        return value
    }

    final override val required: Boolean = true

    final override fun allTokens(): List<String> = super.allTokens()


}
