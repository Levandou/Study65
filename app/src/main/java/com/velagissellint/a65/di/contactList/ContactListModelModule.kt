package com.velagissellint.a65.di.contactList

import androidx.lifecycle.ViewModel
import com.velagissellint.a65.di.ViewModelKey
import com.velagissellint.a65.di.scopes.ContactsListScope
import com.velagissellint.a65.presentation.contactList.ContactListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ContactListModelModule {
    @ContactsListScope
    @Binds
    @IntoMap
    @ViewModelKey(ContactListViewModel::class)
    abstract fun bindViewModelFactory(contactListViewModel: ContactListViewModel): ViewModel
}