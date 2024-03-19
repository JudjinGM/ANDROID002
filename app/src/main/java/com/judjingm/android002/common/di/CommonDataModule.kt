package com.judjingm.android002.common.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.judjingm.android002.common.data.api.NetworkConnectionProvider
import com.judjingm.android002.common.data.impl.NetworkConnectionProviderImpl
import com.judjingm.android002.common.data.network.ErrorInterceptor
import com.judjingm.android002.common.data.network.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun provideOkhttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor,
        errorInterceptor: ErrorInterceptor,
        okkHttpClientBuilder: OkHttpClient.Builder
    ): OkHttpClient {
        return okkHttpClientBuilder
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(headerInterceptor)
            .addInterceptor(errorInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ENDPOINT_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideNetworkConnectionProvider(@ApplicationContext context: Context): NetworkConnectionProvider {
        return NetworkConnectionProviderImpl(context)
    }

    companion object {
        const val ENDPOINT_URL = "https://api.themoviedb.org/"
    }

}