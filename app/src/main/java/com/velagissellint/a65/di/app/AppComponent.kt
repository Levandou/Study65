package com.velagissellint.a65.di.app

import android.app.Application
import com.velagissellint.a65.App
import com.velagissellint.a65.di.contactDetails.ContactDetailsModelComponent
import com.velagissellint.a65.di.contactList.ContactListModelComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun plusContactListComponent(): ContactListModelComponent
    fun plusContactDetailsComponent(): ContactDetailsModelComponent

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}