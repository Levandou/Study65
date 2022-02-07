package com.velagissellint.a65.di.getContactsCase

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.velagissellint.a65.BuildConfig
import com.velagissellint.a65.all.RepositoryImpl
import com.velagissellint.a65.all.api.ApiService
import com.velagissellint.a65.all.data.ContactsRepository
import com.velagissellint.a65.all.db.AppDatabase
import com.velagissellint.a65.all.db.GeoDao
import com.velagissellint.a65.useCase.broadcast.BroadcastRepositoryCase
import com.velagissellint.a65.useCase.contactDetails.ContactDetailsRepositoryCase
import com.velagissellint.a65.useCase.contactListCase.ContactListRepositoryCase
import com.velagissellint.a65.useCase.db.DbRepository
import com.velagissellint.a65.useCase.geo.GeoRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class GetContactsModule {
    @Singleton
    @Provides
    fun getRoomDbInstance(application: Application): AppDatabase =
        Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).allowMainThreadQueries().build()

    @Singleton
    @Provides
    fun getDao(appDatabase: AppDatabase): GeoDao =
        appDatabase.resultDao()

    @Singleton
    @Provides
    fun getRetrofitInstance(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .baseUrl(BuildConfig.SERVER_URL)
            .client(OkHttpClient())
            .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    fun getContentResolver(context: Context) = context.contentResolver

    @Provides
    fun provideListRepository(
        context: Context,
        contactsRepository: ContactsRepository,
        apiService: ApiService,
        geoDao: GeoDao
    ): ContactListRepositoryCase =
        RepositoryImpl(context, contactsRepository, apiService, geoDao)

    @Provides
    fun provideDetailsRepository(
        context: Context,
        contactsRepository: ContactsRepository,
        apiService: ApiService,
        geoDao: GeoDao
    ): ContactDetailsRepositoryCase =
        RepositoryImpl(context, contactsRepository, apiService, geoDao)

    @Provides
    fun provideOffReminder(
        context: Context,
        contactsRepository: ContactsRepository,
        apiService: ApiService,
        geoDao: GeoDao
    ): BroadcastRepositoryCase =
        RepositoryImpl(context, contactsRepository, apiService, geoDao)

    @Provides
    fun provideGeoRepository(
        context: Context,
        contactsRepository: ContactsRepository,
        apiService: ApiService,
        geoDao: GeoDao
    ): GeoRepository = RepositoryImpl(context, contactsRepository, apiService, geoDao)

    @Provides
    fun provideAddToDbRepository(
        context: Context,
        contactsRepository: ContactsRepository,
        apiService: ApiService,
        geoDao: GeoDao
    ): DbRepository = RepositoryImpl(context, contactsRepository, apiService, geoDao)
}
