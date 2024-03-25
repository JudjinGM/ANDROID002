package com.judjingm.android002.details.di

import com.judjingm.android002.common.utill.BaseExceptionToErrorEntityMapper
import com.judjingm.android002.details.data.api.ContentDetailsRemoteDataSource
import com.judjingm.android002.details.data.api.SystemLocalProvider
import com.judjingm.android002.details.data.impl.ContentDetailsRemoteDataSourceImpl
import com.judjingm.android002.details.data.impl.ContentDetailsRepositoryImpl
import com.judjingm.android002.details.data.impl.SystemLocalProviderImpl
import com.judjingm.android002.details.domain.ContentDetailsExceptionToErrorEntityMapper
import com.judjingm.android002.details.domain.repository.ContentDetailsRepository
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

    @Binds
    fun bindSystemLocaleProvider(
        impl: SystemLocalProviderImpl
    ): SystemLocalProvider

    @ContentDetailsExceptionMapper
    @Binds
    fun bindContentDetailsExceptionToErrorMapper(
        impl: ContentDetailsExceptionToErrorEntityMapper
    ): BaseExceptionToErrorEntityMapper


}