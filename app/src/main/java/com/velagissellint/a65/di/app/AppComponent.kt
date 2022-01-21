package com.velagissellint.a65.di.app

import android.app.Application
import com.velagissellint.a65.App
import com.velagissellint.a65.all.containersDi.AppContainer
import com.velagissellint.a65.di.contactDetails.ContactDetailsModelComponent
import com.velagissellint.a65.di.contactList.ContactListModelComponent
import com.velagissellint.a65.di.getContactsCase.GetContactsModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, GetContactsModule::class])
interface AppComponent : AppContainer {

    override fun plusPersonDetailsComponent(): ContactDetailsModelComponent

    override fun plusPersonListComponent(): ContactListModelComponent

    fun inject(app: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}
