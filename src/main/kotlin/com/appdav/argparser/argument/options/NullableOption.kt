package com.appdav.argparser.argument.options

import com.appdav.argparser.argument.ArgumentBaseInternal
import com.appdav.argparser.argument.DelegateArgument
import com.appdav.argparser.argument.TokenizedArgument
import java.util.*
import kotlin.reflect.KProperty

/**
 * NullableOption is an option which value can be null if not passed as an argument. It is also a base class for all other option types
 * Option is a type of argument that is usually passed as a key-value pair separated by the whitespace. Usually it has a short token which is a single hyphen and a letter (i.g. -h)
 * and long token which is a double hyphen and a lowercase words, separated by another hyphens (i.g. --show-help). In arguments passed it usually looks like --file-path /some/file/path
 * Nullable option is not required and has no default value, so if it is not passed with command-line arguments its value will be null.
 */
abstract class NullableOption<T : Any> : ArgumentBaseInternal<T>(),
    TokenizedArgument,
    DelegateArgument<T> {

    override fun getValue(thisRef: Any?, kProperty: KProperty<*>): T? {
        return value
    }

    override val name: String
        get() = token.removePrefix("--").removePrefix("-").replace("-", " ")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    override val required: Boolean = false

    /**
     * @return list of all tokens associated with `this` option
     */
    final override fun allTokens(): List<String> = super.allTokens()

}
