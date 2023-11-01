package com.appdav.argparser.registries

import com.appdav.argparser.argument.options.NullableOption

/**
 * Interface that marks a registry that can provide a DSL for registering Options
 * @see com.appdav.argparser.argument.options.OptionsProvider
 * @see com.appdav.argparser.argument.options.NullableOption
 * @see com.appdav.argparser.argument.options.RequiredOption
 * @see com.appdav.argparser.argument.options.OptionWithDefaultValue
 */
interface OptionRegistryScope {

    /**
     * Provides registered options
     * @return list of registered NullableOption (base class for all options)
     * @see NullableOption
     * @see com.appdav.argparser.argument.options.RequiredOption
     * @see com.appdav.argparser.argument.options.OptionWithDefaultValue
     */
    fun options(): List<NullableOption<*>>

    /**
     * Registers provided option
     * @param option option argument instance
     *  @see NullableOption
     *  @see com.appdav.argparser.argument.options.RequiredOption
     *  @see com.appdav.argparser.argument.options.OptionWithDefaultValue
     */
    fun <E, T : NullableOption<E>> registerOption(option: T): T

}
