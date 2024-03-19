package com.judjingm.android002.home.di

import com.judjingm.android002.common.utill.BaseExceptionToErrorMapper
import com.judjingm.android002.home.data.api.PopularContentRemoteDataSource
import com.judjingm.android002.home.data.impl.PopularContentRemoteDataSourceImpl
import com.judjingm.android002.home.data.impl.PopularContentRepositoryImpl
import com.judjingm.android002.home.domain.PopularContentExceptionToErrorMapper
import com.judjingm.android002.home.domain.repository.PopularContentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PopularContentDataModule {
    @Singleton
    @Binds
    fun bindPopularContentRemoteDataSource(
        impl: PopularContentRemoteDataSourceImpl
    ): PopularContentRemoteDataSource

    @Singleton
    @Binds
    fun bindPopularContentRepository(
        impl: PopularContentRepositoryImpl
    ): PopularContentRepository

    @Binds
    fun bindPopularContentExceptionToErrorMapper(
        impl: PopularContentExceptionToErrorMapper
    ): BaseExceptionToErrorMapper

}