package com.judjingm.android002.details.di

import com.judjingm.android002.common.di.MainAppRetrofit
import com.judjingm.android002.details.data.api.ContentDetailsApiService
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
        @MainAppRetrofit retrofit: Retrofit
    ): ContentDetailsApiService {
        return retrofit.create(ContentDetailsApiService::class.java)
    }

}