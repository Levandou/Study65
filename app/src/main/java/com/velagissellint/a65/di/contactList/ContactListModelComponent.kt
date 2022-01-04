package com.velagissellint.a65.di.contactList

import com.velagissellint.a65.di.ContentResolverModule
import com.velagissellint.a65.di.scopes.ContactsListScope
import com.velagissellint.a65.presentation.contactList.ContactListFragment
import dagger.Subcomponent

@ContactsListScope
@Subcomponent(modules = [ContactListModelModule::class, ContentResolverModule::class])
interface ContactListModelComponent {
    fun inject(fragment: ContactListFragment)
}