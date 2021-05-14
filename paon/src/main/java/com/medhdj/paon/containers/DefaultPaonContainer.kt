package com.medhdj.paon.containers

import com.medhdj.paon.core.PaonComponentsCreator
import com.medhdj.paon.core.PaonComponentsRegistry
import com.medhdj.paon.core.PaonContainer
import com.medhdj.paon.core.PaonProvidersModule
import com.medhdj.paon.creators.ReflectionPaonComponentsCreator
import com.medhdj.paon.registry.ReflectionPaonComponentsRegistry
import kotlin.reflect.KClass

class DefaultPaonContainer private constructor(
    private val componentCreator: PaonComponentsCreator
) : PaonContainer {


    override fun <T : Any> inject(toInject: KClass<T>): T =
        componentCreator.create(toInject) as T

    data class DefaultBuilder(
        val providersModule: PaonProvidersModule,
        val componentsRegistry: PaonComponentsRegistry<KClass<*>> = ReflectionPaonComponentsRegistry(),
        val componentsCreator: PaonComponentsCreator = ReflectionPaonComponentsCreator(
            providersModule,
            componentsRegistry
        )
    ) {
        fun build(): PaonContainer = DefaultPaonContainer(
            componentsCreator
        )
    }
}
