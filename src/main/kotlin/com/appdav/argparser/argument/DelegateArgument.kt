package com.appdav.argparser.argument

import kotlin.reflect.KProperty

/**
 * Marks an argument as a DelegateArgument, which means it can provide its value using by-syntax
 */
interface DelegateArgument<T> {

    /**
     * Delegate operator that returns argument's value using the by-syntax
     */
    operator fun getValue(thisRef: Any?, kProperty: KProperty<*>): T?

}
