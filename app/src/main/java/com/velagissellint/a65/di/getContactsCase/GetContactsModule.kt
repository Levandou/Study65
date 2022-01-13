package com.velagissellint.a65.di.getContactsCase

import android.content.ContentResolver
import android.content.Context
import com.example.domain.useCase.broadcast.BroadcastRepositoryCase
import com.example.domain.useCase.contactDetails.ContactDetailsRepositoryCase
import com.example.domain.useCase.contactListCase.ContactListRepositoryCase
import com.example.libraryy.all.ContactListRepositoryImpl
import com.example.libraryy.all.data.ContactsRepository
import dagger.Module
import dagger.Provides

@Module
class GetContactsModule {

    @Provides
    fun getContentResolver(context: Context) = context.contentResolver

    @Provides
    fun getContactRepository(contentResolver: ContentResolver) = ContactsRepository(contentResolver)

    @Provides
    fun provideListRepository(context: Context,contactsRepository: ContactsRepository): ContactListRepositoryCase =
        ContactListRepositoryImpl(context,contactsRepository)

    @Provides
    fun provideDetailsRepository(context: Context,contactsRepository: ContactsRepository): ContactDetailsRepositoryCase =
        ContactListRepositoryImpl(context,contactsRepository)

    @Provides
    fun provideOffReminder(context: Context,contactsRepository: ContactsRepository): BroadcastRepositoryCase =
        ContactListRepositoryImpl(context,contactsRepository)
}