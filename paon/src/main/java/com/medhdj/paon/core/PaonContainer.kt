package com.medhdj.paon.core

import kotlin.reflect.KClass

/**
 * This is our simple IOC container
 */
interface PaonContainer {
    fun <T : Any> inject(toInject: KClass<T>): T
}
