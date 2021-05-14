package com.medhdj.paon.graphs

import androidx.annotation.VisibleForTesting
import com.medhdj.paon.core.PaonGraph
import com.medhdj.paon.errors.CyclicDependencyError
import com.medhdj.paon.errors.NotRegisteredError
import kotlin.reflect.KClass

/**
 * handle dependencies using a basic map
 */
class BasicPaonGraph : PaonGraph<KClass<*>, KClass<*>> {
    @VisibleForTesting
    val map = mutableMapOf<KClass<*>, MutableList<KClass<*>>>()

    override fun addDependency(dependent: KClass<*>, dependency: KClass<*>) {
        when {
            checkCyclicDependency(dependent, dependency) -> {
                throw CyclicDependencyError("$dependent is creating a circular dependency")
            }

            dependency == Nothing::class -> {
                map[dependent] = mutableListOf()
            }

            else -> {
                map[dependent]?.add(dependency) ?: run {
                    map[dependent] = mutableListOf(dependency)
                }
            }
        }
    }

    override fun getDependencies(dependent: KClass<*>): List<KClass<*>> =
        map[dependent] ?: throw NotRegisteredError("$dependent is not registered")

    /**
     * using a reverse lookup algorithm, inspired by the Breadth-first search
     */
    override fun checkCyclicDependency(dependent: KClass<*>, dependency: KClass<*>): Boolean {
        return when (dependent) {
            Nothing::class -> false
            dependency -> true
            else -> {
                val directLookupMap = map.filter { it.value.contains(dependent) }
                when {
                    directLookupMap.isEmpty() -> {
                        return false
                    }
                    directLookupMap.keys.contains(dependency) -> {
                        return true
                    }
                    else -> {
                        val unDirectLookupMap = map.filter { unDirectEntry ->
                            unDirectEntry.value.any {
                                directLookupMap.keys.contains(it)
                            }
                        }
                        return unDirectLookupMap.keys.contains(dependency)
                    }
                }
            }
        }
    }

    override fun hasDependent(dependent: KClass<*>): Boolean =
        map.keys.contains(dependent)
}
