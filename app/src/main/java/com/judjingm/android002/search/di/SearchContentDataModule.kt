package com.judjingm.android002.search.di

import com.judjingm.android002.common.utill.BaseExceptionToErrorEntityMapper
import com.judjingm.android002.search.data.api.SearchContentRemoteDataSource
import com.judjingm.android002.search.data.impl.SearchContentRemoteDataSourceImpl
import com.judjingm.android002.search.data.impl.SearchContentRepositoryImpl
import com.judjingm.android002.search.domain.SearchContentExceptionToErrorEntityMapper
import com.judjingm.android002.search.domain.repository.SearchContentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SearchContentExceptionMapper

@Module
@InstallIn(SingletonComponent::class)
interface SearchContentDataModule {
    @Singleton
    @Binds
    fun bindSearchContentRemoteDataSource(
        impl: SearchContentRemoteDataSourceImpl
    ): SearchContentRemoteDataSource

    @Singleton
    @Binds
    fun bindSearchContentRepository(
        impl: SearchContentRepositoryImpl
    ): SearchContentRepository

    @SearchContentExceptionMapper
    @Binds
    fun bindSearchContentExceptionToErrorMapper(
        impl: SearchContentExceptionToErrorEntityMapper
    ): BaseExceptionToErrorEntityMapper

}