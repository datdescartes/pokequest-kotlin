package com.arya21.pokequest

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import com.arya21.pokequest.di.AppInjector
import timber.log.Timber
import javax.inject.Inject

class App: Application(), HasAndroidInjector {
    companion object {
        const val BASE_URL = "https://pokequestapi.herokuapp.com/"
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this, BASE_URL)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // TODO log/crash report here
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}