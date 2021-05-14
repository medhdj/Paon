package com.medhdj.paon.registry

import com.medhdj.paon.annotations.PaonInject
import com.medhdj.paon.core.PaonComponentsRegistry
import com.medhdj.paon.core.PaonGraph
import com.medhdj.paon.errors.DependencyResolutionError
import com.medhdj.paon.graphs.BasicPaonGraph
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation

class ReflectionPaonComponentsRegistry(
    override val dependenciesGraph: PaonGraph<KClass<*>, KClass<*>> = BasicPaonGraph()
) : PaonComponentsRegistry<KClass<*>> {
    override fun register(toRegister: KClass<*>) {
        if (dependenciesGraph.hasDependent(toRegister).not()) {
            val dependenciesList = resolveDependencies(toRegister)
            if (dependenciesList.isNullOrEmpty()) {
                dependenciesGraph.addDependency(toRegister, Nothing::class)
            } else {
                dependenciesList.forEach {
                    dependenciesGraph.addDependency(toRegister, it)
                }
            }
        }
    }

    override fun unregister(toUnRegister: KClass<*>) {
        TODO("Not yet implemented")
    }

    override fun findDependencies(toFind: KClass<*>) =
        dependenciesGraph.getDependencies(toFind)


    private fun resolveDependencies(toRegister: KClass<*>): List<KClass<*>> =
        toRegister.constructors.filter {
            it.hasAnnotation<PaonInject>()
        }.apply {
            if (isNullOrEmpty()) {
                throw DependencyResolutionError(
                    "No @PaonInject constructor found," +
                            " You must add constructor with @PaonInject annotation"
                )
            }
        }.flatMap {
            it.parameters
        }.distinct().mapNotNull {
            generateClass(it)
        }.toList()


    /**
     * function found on internet + modifications
     */
    private fun generateClass(param: KParameter): KClass<*>? {
        var result: KClass<*>? = null
        val paramName = param.type.toString()

        if (paramName.startsWith("kotlin.collections.List")) {
            val itemTypeName = paramName.substring(24).removeSuffix(">")
            if (!itemTypeName.startsWith("kotlin")) {
                result = param.type.arguments[0].type?.classifier as KClass<*>
            }
        } else if (!paramName.startsWith("kotlin")) {
            result = param.type.classifier as KClass<*>
        }

        return result
    }


}