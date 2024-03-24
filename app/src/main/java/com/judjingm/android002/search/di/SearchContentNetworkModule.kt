package com.judjingm.android002.search.di

import com.judjingm.android002.search.data.api.SearchContentApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class SearchContentNetworkModule {
    @Provides
    @Singleton
    fun provideSearchContentApiService(
        retrofit: Retrofit
    ): SearchContentApiService {
        return retrofit.create(SearchContentApiService::class.java)
    }

}