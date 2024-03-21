package com.judjingm.android002.content.di

import com.judjingm.android002.content.data.api.ContentDetailsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ContentDetailsNetworkModule {
    @Provides
    @Singleton
    fun provideContentDetailsApiService(
        retrofit: Retrofit
    ): ContentDetailsApiService {
        return retrofit.create(ContentDetailsApiService::class.java)
    }

}