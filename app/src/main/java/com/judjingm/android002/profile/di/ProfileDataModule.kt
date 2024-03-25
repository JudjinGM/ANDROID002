package com.judjingm.android002.profile.di

import com.judjingm.android002.common.utill.BaseExceptionToErrorEntityMapper
import com.judjingm.android002.profile.data.api.ProfileRemoteDataSource
import com.judjingm.android002.profile.data.impl.ProfileRemoteDataSourceImpl
import com.judjingm.android002.profile.data.impl.ProfileRepositoryImpl
import com.judjingm.android002.profile.domain.ProfileExceptionToErrorEntityMapper
import com.judjingm.android002.profile.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProfileExceptionMapper

@Module
@InstallIn(SingletonComponent::class)
interface ProfileDataModule {
    @Singleton
    @Binds
    fun bindProfileRemoteDataSource(
        impl: ProfileRemoteDataSourceImpl
    ): ProfileRemoteDataSource

    @Singleton
    @Binds
    fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository

    @ProfileExceptionMapper
    @Binds
    fun bindProfileExceptionToErrorMapper(
        impl: ProfileExceptionToErrorEntityMapper
    ): BaseExceptionToErrorEntityMapper
}