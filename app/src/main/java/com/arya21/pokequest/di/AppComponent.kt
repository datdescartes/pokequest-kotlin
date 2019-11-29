package com.arya21.pokequest.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import com.arya21.pokequest.App
import com.arya21.pokequest.data.DataModule
import com.arya21.pokequest.presentation.BuildersModule
import com.arya21.pokequest.presentation.ViewModelModule
import javax.inject.Named
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    DataModule::class,
    BuildersModule::class,
    ViewModelModule::class
])
interface AppComponent : AndroidInjector<App> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application,
                   @BindsInstance @Named("baseUrl") baseUrl: String): AppComponent
    }
}