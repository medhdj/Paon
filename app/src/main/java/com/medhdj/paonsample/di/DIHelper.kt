package com.medhdj.paonsample.di

import com.medhdj.paon.containers.DefaultPaonContainer
import com.medhdj.paon.core.PaonContainer
import com.medhdj.paon.registry.ReflectionPaonComponentsRegistry
import com.medhdj.paonsample.utils.DummyClass1
import com.medhdj.paonsample.utils.DummyClass2

object DIHelper {
    val componentsRegistry = ReflectionPaonComponentsRegistry()

    lateinit var ponContainer: PaonContainer

    fun init() {
        componentsRegistry.register(DummyClass1::class)
        componentsRegistry.register(DummyClass2::class)

        ponContainer = DefaultPaonContainer.DefaultBuilder(
            providersModule = SamplePaonProvidersModule,
            componentsRegistry = componentsRegistry
        ).build()
    }
}