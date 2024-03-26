package com.judjingm.android002.profile.di

import com.judjingm.android002.profile.data.api.AuthenticationApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AuthenticationNetworkModule {
    @Provides
    @Singleton
    fun provideProfileApiService(
        retrofit: Retrofit
    ): AuthenticationApiService {
        return retrofit.create(AuthenticationApiService::class.java)
    }

}