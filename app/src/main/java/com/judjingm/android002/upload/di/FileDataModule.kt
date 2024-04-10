package com.judjingm.android002.upload.di

import com.judjingm.android002.upload.data.api.FileLocalStorage
import com.judjingm.android002.upload.data.api.FileRemoteDataSource
import com.judjingm.android002.upload.data.impl.FileLocalStorageImpl
import com.judjingm.android002.upload.data.impl.FileRemoteDataSourceImpl
import com.judjingm.android002.upload.data.impl.FileRepositoryImpl
import com.judjingm.android002.upload.domain.repository.FileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface FileDataModule {
    @Singleton
    @Binds
    fun bindFileLocalStorage(impl: FileLocalStorageImpl): FileLocalStorage

    @Singleton
    @Binds
    fun bindFileRemoteDataSource(impl: FileRemoteDataSourceImpl): FileRemoteDataSource

    @Singleton
    @Binds
    fun bindFileRepository(impl: FileRepositoryImpl): FileRepository
}