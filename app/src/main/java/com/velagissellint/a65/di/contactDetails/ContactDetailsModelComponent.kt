package com.velagissellint.a65.di.contactDetails

import com.velagissellint.a65.di.ContentResolverModule
import com.velagissellint.a65.di.scopes.ContactsDetailsScope
import com.velagissellint.a65.presentation.contactDetails.ContactDetailsFragment
import dagger.Subcomponent

@ContactsDetailsScope
@Subcomponent(modules = [ContactDetailsModelModule::class, ContentResolverModule::class])
interface ContactDetailsModelComponent {
    fun inject(fragment: ContactDetailsFragment)
}