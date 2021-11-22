package com.velagissellint.a65

import android.content.ContentResolver
import android.content.Context
import com.velagissellint.a65.data.ContactsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class Module {
    @Singleton
    @Provides
    fun getContentResolver(@ApplicationContext appContext: Context)= appContext.contentResolver

    @Singleton
    @Provides
    fun getRepositoryOfContacts(contentResolver: ContentResolver)
    :ContactsRepository= ContactsRepository(contentResolver)
}
