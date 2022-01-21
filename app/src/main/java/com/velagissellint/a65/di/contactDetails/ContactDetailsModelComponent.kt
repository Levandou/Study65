package com.velagissellint.a65.di.contactDetails

import com.velagissellint.a65.ContentResolverModule
import com.velagissellint.a65.all.containersDi.ContactDetailsContainer
import com.velagissellint.a65.scopes.ContactsDetailsScope
import dagger.Subcomponent

@ContactsDetailsScope
@Subcomponent(modules = [ContactDetailsModelModule::class, ContentResolverModule::class])
interface ContactDetailsModelComponent : ContactDetailsContainer
