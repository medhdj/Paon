package com.medhdj.paon.graphs

import com.medhdj.paon.errors.CyclicDependencyError
import com.medhdj.paon.errors.NotRegisteredError
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.shouldBeEmpty
import org.junit.Test

class BasicPaonGraphTest {
    private val tested = BasicPaonGraph()

    @Test
    fun `test adding correct dependencies`() {
        // given
        // we assume A -> B,C, and C -> D
        class TestClassA
        class TestClassB
        class TestClassC
        class TestClassD


        // when
        tested.addDependency(TestClassA::class, TestClassB::class)
        tested.addDependency(TestClassA::class, TestClassC::class)
        tested.addDependency(TestClassC::class, TestClassD::class)

        // then
        tested.map.size `should be equal to` 2
    }

    @Test
    fun `test getting dependencies correctly`() {
        // given

        // we assume we have the following dependency schema
        //      1 -> 2,3,4
        //      2 -> 5,6
        //      4 -> 5,6

        class TestClass1
        class TestClass2
        class TestClass3
        class TestClass4
        class TestClass5
        class TestClass6
        class TestClass7
        tested.map[TestClass1::class] =
            mutableListOf(TestClass2::class, TestClass3::class, TestClass4::class)
        tested.map[TestClass2::class] =
            mutableListOf(TestClass5::class, TestClass6::class)
        tested.map[TestClass4::class] =
            mutableListOf(TestClass5::class, TestClass7::class)
        tested.map[TestClass5::class] = mutableListOf()
        // when

        val result1 = tested.getDependencies(TestClass1::class)
        val result2 = tested.getDependencies(TestClass4::class)
        val result3 = tested.getDependencies(TestClass5::class)

        // then
        result1 `should be equal to` listOf(TestClass2::class, TestClass3::class, TestClass4::class)
        result2.size `should be equal to` 2
        result3.shouldBeEmpty()

    }

    @Test(expected = NotRegisteredError::class)
    fun `test we throw an exception if the component is not registered`() {
        // given

        // we assume we have the following dependency schema
        //      1 -> 2,3,4

        class TestClass1
        class TestClass2
        class TestClass3
        class TestClass4

        tested.map[TestClass1::class] =
            mutableListOf(TestClass2::class, TestClass3::class, TestClass4::class)

        // when
        tested.getDependencies(TestClass4::class)

        // then
        // should throw exception

    }

    @Test
    fun `test that checkCyclicDependency correctly detect circular dependencies`() {
        // given

        // we assume we have the following dependency schema
        //      1 -> 2,3,4
        //      2 -> 5,6
        //      4 -> 5,6

        class TestClass1
        class TestClass2
        class TestClass3
        class TestClass4
        class TestClass5
        class TestClass6
        class TestClass7
        tested.map[TestClass1::class] =
            mutableListOf(TestClass2::class, TestClass3::class, TestClass4::class)
        tested.map[TestClass2::class] =
            mutableListOf(TestClass5::class, TestClass6::class)
        tested.map[TestClass4::class] =
            mutableListOf(TestClass5::class, TestClass7::class)

        // when

        val result1 = tested.checkCyclicDependency(TestClass5::class, TestClass1::class)
        val result2 = tested.checkCyclicDependency(TestClass5::class, TestClass4::class)
        val result3 = tested.checkCyclicDependency(TestClass5::class, TestClass7::class)
        val result4 = tested.checkCyclicDependency(TestClass5::class, TestClass5::class)

        // then
        result1 `should be equal to` true
        result2 `should be equal to` true
        result3 `should be equal to` false
        result4 `should be equal to` true
    }

    @Test(expected = CyclicDependencyError::class)
    fun `test that adding a circular dependency throws an error`() {
        // given

        // we assume we have the following dependency schema
        //      1 -> 2,3,4
        //      2 -> 5,6
        //      4 -> 5,6

        class TestClass1
        class TestClass2
        class TestClass3
        class TestClass4
        class TestClass5
        class TestClass6
        class TestClass7
        tested.map[TestClass1::class] =
            mutableListOf(TestClass2::class, TestClass3::class, TestClass4::class)
        tested.map[TestClass2::class] =
            mutableListOf(TestClass5::class, TestClass6::class)
        tested.map[TestClass4::class] =
            mutableListOf(TestClass5::class, TestClass7::class)

        // when

        tested.addDependency(TestClass5::class, TestClass1::class)

        // then
        // should throw CyclicDependencyError
    }
}
