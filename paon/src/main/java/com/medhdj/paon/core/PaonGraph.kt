package com.medhdj.paon.core

/**
 * represents the dependency graph
 * a DEPENDENT will need a DEPENDENCY to be able to work correctly
 */
interface PaonGraph<DEPENDENT, DEPENDENCY> {
    fun addDependency(
        dependent: DEPENDENT,
        dependency: DEPENDENCY
    )

    fun getDependencies(dependent: DEPENDENT): List<DEPENDENCY>

    fun hasDependent(dependent: DEPENDENT): Boolean

    /**
     * check for any possible circular dependendy using the dependent and it's dependency
     */
    fun checkCyclicDependency(
        dependent: DEPENDENT,
        dependency: DEPENDENCY
    ): Boolean
}