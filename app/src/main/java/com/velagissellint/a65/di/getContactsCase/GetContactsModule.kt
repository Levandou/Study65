package com.velagissellint.a65.di.getContactsCase

import android.content.ContentResolver
import android.content.Context
import com.velagissellint.a65.useCase.broadcast.BroadcastRepositoryCase
import com.velagissellint.a65.useCase.contactDetails.ContactDetailsRepositoryCase
import com.velagissellint.a65.useCase.contactListCase.ContactListRepositoryCase
import com.velagissellint.a65.all.RepositoryImpl
import com.velagissellint.a65.all.data.ContactsRepository
import dagger.Module
import dagger.Provides

@Module
class GetContactsModule {

    @Provides
    fun getContentResolver(context: Context) = context.contentResolver

    @Provides
    fun getContactRepository(contentResolver: ContentResolver) =
        ContactsRepository(contentResolver)

    @Provides
    fun provideListRepository(
        context: Context,
        contactsRepository: ContactsRepository
    ): ContactListRepositoryCase =
        RepositoryImpl(context, contactsRepository)

    @Provides
    fun provideDetailsRepository(
        context: Context,
        contactsRepository: ContactsRepository
    ): ContactDetailsRepositoryCase =
        RepositoryImpl(context, contactsRepository)

    @Provides
    fun provideOffReminder(
        context: Context,
        contactsRepository: ContactsRepository
    ): BroadcastRepositoryCase =
        RepositoryImpl(context, contactsRepository)
}