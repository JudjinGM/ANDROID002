package com.judjingm.android002.profile.di

import com.judjingm.android002.common.utill.BaseExceptionToErrorEntityMapper
import com.judjingm.android002.profile.data.api.AuthenticationRemoteDataSource
import com.judjingm.android002.profile.data.impl.AuthenticationRemoteDataSourceImpl
import com.judjingm.android002.profile.data.impl.AuthenticationRepositoryImpl
import com.judjingm.android002.profile.domain.AuthenticationExceptionToErrorEntityMapper
import com.judjingm.android002.profile.domain.repository.AuthenticationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthenticationExceptionMapper

@Module
@InstallIn(SingletonComponent::class)
interface ProfileDataModule {
    @Singleton
    @Binds
    fun bindProfileRemoteDataSource(
        impl: AuthenticationRemoteDataSourceImpl
    ): AuthenticationRemoteDataSource

    @Singleton
    @Binds
    fun bindProfileRepository(
        impl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @AuthenticationExceptionMapper
    @Binds
    fun bindProfileExceptionToErrorMapper(
        impl: AuthenticationExceptionToErrorEntityMapper
    ): BaseExceptionToErrorEntityMapper
}