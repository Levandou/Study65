package com.velagissellint.a65.di.contactDetails

import androidx.lifecycle.ViewModel
import com.velagissellint.a65.ViewModelKey
import com.velagissellint.a65.scopes.ContactsDetailsScope
import com.velagissellint.a65.all.presentation.contactDetails.ContactDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ContactDetailsModelModule {
    @ContactsDetailsScope
    @Binds
    @IntoMap
    @ViewModelKey(ContactDetailsViewModel::class)
    abstract fun bindViewModelFactory(contactDetailsViewModel: ContactDetailsViewModel): ViewModel
}