package com.velagissellint.a65.di.getContactsCase

import com.velagissellint.a65.ContentResolverModule
import com.velagissellint.a65.di.contactList.ContactListModelModule
import com.velagissellint.a65.scopes.ContactsListScope
import dagger.Subcomponent

@ContactsListScope
@Subcomponent(modules = [ContactListModelModule::class, ContentResolverModule::class])
interface GetContactsComponent
