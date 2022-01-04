package com.velagissellint.a65.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContentResolverModule {
    @Provides
    fun getContentResolver(context: Context) = context.contentResolver
}