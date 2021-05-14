package com.medhdj.paonsample.di

import com.medhdj.paon.annotations.PaonProvides
import com.medhdj.paon.core.PaonProvidersModule
import com.medhdj.paonsample.utils.DummyClass1
import com.medhdj.paonsample.utils.DummyClass2

object SamplePaonProvidersModule : PaonProvidersModule {

    @PaonProvides
    fun provideDummyClass2() = DummyClass2()

    @PaonProvides
    fun provideDummyClass1(c2: DummyClass2) = DummyClass1(c2)

}