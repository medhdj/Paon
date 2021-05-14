package com.medhdj.paon.registry

import com.medhdj.paon.annotations.PaonInject
import com.medhdj.paon.errors.DependencyResolutionError
import org.amshove.kluent.`should be equal to`
import org.junit.Test

class ReflectionPaonComponentsRegistryTest {
    private val tested = ReflectionPaonComponentsRegistry()

    @Test
    fun `test correct register of a new component`() {
        // given
        class TestClass4
        class TestClass3
        class TestClass2
        class TestClass1 @PaonInject constructor(
            val c1: TestClass2,
            val c3: TestClass3,
            val someInt: Int,
            val someString: String,
            val c4: TestClass4
        )

        // when
        tested.register(TestClass1::class)
        val dependencies = tested.dependenciesGraph.getDependencies(TestClass1::class)

        // then
        dependencies.size `should be equal to` 3
        dependencies[0] `should be equal to` TestClass2::class
        dependencies[1] `should be equal to` TestClass3::class
        dependencies[2] `should be equal to` TestClass4::class
    }

    @Test
    fun `test ignore registration for already registered components`() {
        // given
        class TestClass4
        class TestClass3
        class TestClass2
        class TestClass1 @PaonInject constructor(
            val c1: TestClass2,
            val c3: TestClass3,
            val someInt: Int,
            val someString: String,
            val c4: TestClass4
        )

        // when
        tested.register(TestClass1::class)
        tested.register(TestClass1::class)
        val dependencies = tested.dependenciesGraph.getDependencies(TestClass1::class)

        // then
        dependencies.size `should be equal to` 3
    }

    @Test(expected = DependencyResolutionError::class)
    fun `should throw an error when registering a component without PaonInject annotation`() {
        // given
        class TestClass3
        class TestClass2
        class TestClass1 constructor(val c1: TestClass2, val c3: TestClass3)

        // when
        tested.register(TestClass1::class)

        // then
        // should throw DependencyResolutionError
    }
}