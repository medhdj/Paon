package com.medhdj.paon.core

import kotlin.reflect.KClass

/**
 * will resolve and register all the REGISTRABLE dependencies in the PaonGraph
 */
interface PaonComponentsRegistry<in COMPONENT : Any> {
    val dependenciesGraph: PaonGraph<*, *>

    fun register(toRegister: COMPONENT)

    fun unregister(toUnRegister: COMPONENT)

    fun findDependencies(toFind: COMPONENT): List<KClass<*>>
}