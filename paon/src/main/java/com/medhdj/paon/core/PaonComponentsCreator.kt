package com.medhdj.paon.core

import kotlin.reflect.KClass

interface PaonComponentsCreator {
    fun create(toCreate: KClass<*>): Any
}
