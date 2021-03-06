package com.velagissellint.a65.di.contactList

import androidx.lifecycle.ViewModel
import com.velagissellint.a65.ViewModelKey
import com.velagissellint.a65.all.presentation.contactList.ContactListViewModel
import com.velagissellint.a65.scopes.ContactsListScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
// abstract class
interface ContactListModelModule {
    @ContactsListScope
    @Binds
    @IntoMap
    @ViewModelKey(ContactListViewModel::class)
    // abstract
    fun bindViewModelFactory(contactListViewModel: ContactListViewModel): ViewModel
}
