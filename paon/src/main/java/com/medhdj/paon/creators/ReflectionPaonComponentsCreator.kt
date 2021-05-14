package com.medhdj.paon.creators

import androidx.annotation.VisibleForTesting
import com.medhdj.paon.annotations.PaonProvides
import com.medhdj.paon.core.PaonComponentsCreator
import com.medhdj.paon.core.PaonComponentsRegistry
import com.medhdj.paon.core.PaonProvidersModule
import com.medhdj.paon.errors.DependencyResolutionError
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.valueParameters

class ReflectionPaonComponentsCreator(
    private val providersModule: PaonProvidersModule,
    private val componentsRegistry: PaonComponentsRegistry<KClass<*>>
) : PaonComponentsCreator {

    @VisibleForTesting
    val providersCache = mutableMapOf<KClass<*>, KFunction<*>>()

    override fun create(toCreate: KClass<*>): Any {
        val provider =
            providersCache[toCreate] ?: createProviderAndSetCache(toCreate, providersModule)

        val providerParams = resolveProviderParameters(provider)


        return provider.call(providersModule, *providerParams.toTypedArray())
            ?: throw DependencyResolutionError("can't create $toCreate")
    }


    private fun createProviderAndSetCache(
        toCreate: KClass<*>,
        providersModule: PaonProvidersModule
    ): KFunction<*> {
        val paonProvidesMethods = providersModule::class.functions.filter {
            it.hasAnnotation<PaonProvides>() && it.returnType.classifier == toCreate
        }.apply {

            if (isNullOrEmpty()) {
                throw DependencyResolutionError("No @PaonProvides method found for $toCreate")
            }
            if (size > 1) {
                throw DependencyResolutionError("Multiple providing for $toCreate")
            }
        }
        // add all dependencies providers
        componentsRegistry.findDependencies(toCreate).forEach {
            providersCache[it] ?: createProviderAndSetCache(it, providersModule)
        }

        paonProvidesMethods.elementAt(0)
            .apply {
                providersCache[toCreate] = this
                return this
            }
    }

    private fun resolveProviderParameters(provider: KFunction<Any?>): List<Any> =
        provider.valueParameters.mapNotNull {
            when (it.kind) {
                KParameter.Kind.INSTANCE -> providersModule
                KParameter.Kind.VALUE -> {
                    val paramProvider = providersCache[it.type.classifier as KClass<*>]
                    val paramList: List<Any> = when {
                        paramProvider?.valueParameters.isNullOrEmpty().not() -> {
                            resolveProviderParameters(paramProvider as KFunction<Any?>)
                        }
                        else -> emptyList()
                    }
                    if (paramList.isNullOrEmpty().not()) {
                        paramProvider?.call(providersModule, paramList)
                    } else {
                        paramProvider?.call(providersModule)
                    }

                }
                else -> null
            }
        }
}
