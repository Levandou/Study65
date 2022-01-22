package com.velagissellint.a65.di.contactList

import com.velagissellint.a65.all.containersDi.ContactListContainer
import com.velagissellint.a65.ContentResolverModule
import com.velagissellint.a65.scopes.ContactsListScope
import dagger.Subcomponent

@ContactsListScope
@Subcomponent(modules = [ContactListModelModule::class, ContentResolverModule::class])
interface ContactListModelComponent : ContactListContainer