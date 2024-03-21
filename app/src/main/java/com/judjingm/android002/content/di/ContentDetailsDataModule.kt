package com.judjingm.android002.content.di

import com.judjingm.android002.common.utill.BaseExceptionToErrorEntityMapper
import com.judjingm.android002.content.data.api.ContentDetailsRemoteDataSource
import com.judjingm.android002.content.data.impl.ContentDetailsRemoteDataSourceImpl
import com.judjingm.android002.content.data.impl.ContentDetailsRepositoryImpl
import com.judjingm.android002.content.domain.ContentDetailsExceptionToErrorEntityMapper
import com.judjingm.android002.content.domain.repository.ContentDetailsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ContentDetailsExceptionMapper

@Module
@InstallIn(SingletonComponent::class)
interface ContentDetailsDataModule {
    @Singleton
    @Binds
    fun bindContentDetailRemoteDataSource(
        impl: ContentDetailsRemoteDataSourceImpl
    ): ContentDetailsRemoteDataSource

    @Singleton
    @Binds
    fun bindContentDetailRepository(
        impl: ContentDetailsRepositoryImpl
    ): ContentDetailsRepository

    @ContentDetailsExceptionMapper
    @Binds
    fun bindContentDetailsExceptionToErrorMapper(
        impl: ContentDetailsExceptionToErrorEntityMapper
    ): BaseExceptionToErrorEntityMapper

}