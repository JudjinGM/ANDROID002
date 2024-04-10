package com.judjingm.android002.upload.di

import com.judjingm.android002.common.di.InternalServerRetrofit
import com.judjingm.android002.upload.data.api.FileApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FileNetworkModule {
    @Provides
    @Singleton
    fun provideFileApiService(
        @InternalServerRetrofit retrofit: Retrofit
    ): FileApiService {
        return retrofit.create(FileApiService::class.java)
    }

}