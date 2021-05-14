# Paon
Small Kotlin dependency injection library, only for learning et testing purposes

### How to use:
1 create a PaonProvidersModule to list and provide dependecies:
```kotlin
object SamplePaonProvidersModule : PaonProvidersModule {

    @PaonProvides
    fun provideDummyClass2() = DummyClass2()

    @PaonProvides
    fun provideDummyClass1(c2: DummyClass2) = DummyClass1(c2)
}
```
2 create a `PaonComponentsRegistry` and start to register the classes you need to inject
```kotlin
val componentsRegistry = ReflectionPaonComponentsRegistry()
componentsRegistry.register(DummyClass1::class)
componentsRegistry.register(DummyClass2::class)
```
3 create your `PaonContainer` and start using it
```kotlin
val ponContainer = DefaultPaonContainer.DefaultBuilder(
            providersModule = SamplePaonProvidersModule,
            componentsRegistry = componentsRegistry
        ).build()
...
...
val dummy1 = ponContainer.inject(DummyClass1::class)
```

That's it, Happy injecting :grin:
