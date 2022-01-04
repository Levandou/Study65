package com.velagissellint.a65.di.contactDetails

import androidx.lifecycle.ViewModel
import com.velagissellint.a65.di.ViewModelKey
import com.velagissellint.a65.di.scopes.ContactsDetailsScope
import com.velagissellint.a65.presentation.contactDetails.ContactDetailsViewModel
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