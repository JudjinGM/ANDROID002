package com.judjingm.android002.home.di

import com.judjingm.android002.common.di.MainAppRetrofit
import com.judjingm.android002.home.data.api.PopularContentApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class PopularContentNetworkModule {
    @Provides
    @Singleton
    fun providePopularContentApiService(
        @MainAppRetrofit retrofit: Retrofit
    ): PopularContentApiService {
        return retrofit.create(PopularContentApiService::class.java)
    }

}