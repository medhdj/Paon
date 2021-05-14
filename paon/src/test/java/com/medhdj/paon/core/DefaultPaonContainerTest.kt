package com.medhdj.paon.core

import com.medhdj.paon.annotations.PaonInject
import com.medhdj.paon.annotations.PaonProvides
import com.medhdj.paon.containers.DefaultPaonContainer
import com.medhdj.paon.errors.NotRegisteredError
import com.medhdj.paon.registry.ReflectionPaonComponentsRegistry
import org.amshove.kluent.`should be instance of`
import org.junit.Test

class DefaultPaonContainerTest {

    class TestClass2 @PaonInject constructor()
    class TestClass1 @PaonInject constructor(val testClass2: TestClass2)

    private val providersModule = object : PaonProvidersModule {
        @PaonProvides
        fun provideTestClass2() = TestClass2()

        @PaonProvides
        fun provideTestClass1(c2: TestClass2) = TestClass1(c2)
    }
    private val componentsRegistry = ReflectionPaonComponentsRegistry()

    val tested = DefaultPaonContainer.DefaultBuilder(
        providersModule = providersModule,
        componentsRegistry = componentsRegistry
    ).build()

    @Test
    fun `test correct injection`() {
        // given
        componentsRegistry.register(TestClass1::class)
        componentsRegistry.register(TestClass2::class)

        // when
        val result = tested.inject(TestClass1::class)

        // then
        result `should be instance of` TestClass1::class
    }

    @Test(expected = NotRegisteredError::class)
    fun `test should throw non registered error`() {
        // given
        componentsRegistry.register(TestClass1::class)

        // when
        val result = tested.inject(TestClass1::class)

        // then
        result `should be instance of` TestClass1::class
    }
}
