package com.velagissellint.a65

import android.app.Application
import com.velagissellint.a65.di.app.AppComponent
import com.velagissellint.a65.di.app.DaggerAppComponent

lateinit var applicationComponent: AppComponent

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerAppComponent.builder().application(this).build()
        applicationComponent.inject(this)
    }
}