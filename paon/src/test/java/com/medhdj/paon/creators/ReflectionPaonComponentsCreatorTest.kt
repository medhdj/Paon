package com.medhdj.paon.creators

import com.medhdj.paon.annotations.PaonInject
import com.medhdj.paon.annotations.PaonProvides
import com.medhdj.paon.core.PaonProvidersModule
import com.medhdj.paon.errors.NotRegisteredError
import com.medhdj.paon.registry.ReflectionPaonComponentsRegistry
import org.amshove.kluent.`should be instance of`
import org.junit.Test

class ReflectionPaonComponentsCreatorTest {
    // test classes
    class TestClass2 @PaonInject constructor()
    class TestClass1 @PaonInject constructor(string: String, c2: TestClass2)

    private val providersModule = object : PaonProvidersModule {
        @PaonProvides
        fun provideTestClass2() = TestClass2()

        @PaonProvides
        fun provideTestClass1(c2: TestClass2) = TestClass1("ABC", c2)
    }

    private val componentsRegistry = ReflectionPaonComponentsRegistry()
    private val tested = ReflectionPaonComponentsCreator(providersModule, componentsRegistry)


    @Test
    fun `test correct instance creation`() {
        // given
        componentsRegistry.register(TestClass1::class)
        componentsRegistry.register(TestClass2::class)

        // when
        val instance1 = tested.create(TestClass1::class)
        val instance2 = tested.create(TestClass2::class)

        // then
        instance1 `should be instance of` TestClass1::class
        instance2 `should be instance of` TestClass2::class
    }

    @Test(expected = NotRegisteredError::class)
    fun `test should throw error for non registered components`() {
        // given
        componentsRegistry.register(TestClass1::class)

        // when
        val instance1 = tested.create(TestClass1::class)
        val instance2 = tested.create(TestClass2::class)

        // then
        instance1 `should be instance of` TestClass1::class
        instance2 `should be instance of` TestClass2::class
    }
}
