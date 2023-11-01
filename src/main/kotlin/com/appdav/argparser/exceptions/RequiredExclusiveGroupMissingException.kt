package com.appdav.argparser.exceptions

import com.appdav.argparser.registries.MutuallyExclusiveGroup
import com.appdav.argparser.argument.TokenizedArgument

/**
 * Occurs when registered mutually exclusive group is marked as required, but no argument from this group is initialized
 */
class RequiredExclusiveGroupMissingException(group: MutuallyExclusiveGroup) :
    IllegalArgumentException("Mutually exclusive group is marked as required, which means that one of its arguments must be passed\n" +
            "Please pass one of those: ${group.joinToString(", ") { (it as TokenizedArgument).token }}"
    )
