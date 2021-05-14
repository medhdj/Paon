package com.medhdj.paonsample.utils

import com.medhdj.paon.annotations.PaonInject

class DummyClass1 @PaonInject constructor(private val dummyClass2: DummyClass2) {
    fun getText() = "DummyClass1 got : ${dummyClass2.getText()}"
}