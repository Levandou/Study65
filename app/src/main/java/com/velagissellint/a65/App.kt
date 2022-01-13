package com.velagissellint.a65

import android.app.Application
import com.velagissellint.a65.all.containersDi.AppContainer
import com.velagissellint.a65.all.containersDi.ContainerAppContainer
import com.velagissellint.a65.di.app.AppComponent
import com.velagissellint.a65.di.app.DaggerAppComponent

lateinit var applicationComponent: AppComponent

class App : Application(), ContainerAppContainer {
    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerAppComponent.builder().application(this).build()
    }

    override fun appContainer(): AppContainer = applicationComponent
}