package com.judjingm.android002.upload.di

import com.judjingm.android002.upload.domain.useCase.GetFileNameUseCase
import com.judjingm.android002.upload.domain.useCase.SavePdfToPrivateStorageUseCase
import com.judjingm.android002.upload.domain.useCase.SetFileNameUseCase
import com.judjingm.android002.upload.domain.useCase.UploadPdfToServerUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface FileDomainModule {

    @Binds
    fun bindSavePdfToPrivateStorageUseCase(
        impl: SavePdfToPrivateStorageUseCase.Base
    ): SavePdfToPrivateStorageUseCase

    @Binds
    fun bindUploadPdfToServerUseCase(
        impl: UploadPdfToServerUseCase.Base
    ): UploadPdfToServerUseCase

    @Binds
    fun bindGetFileNameUseCase(
        impl: GetFileNameUseCase.Base
    ): GetFileNameUseCase

    @Binds
    fun bindSetFileNameUseCase(
        impl: SetFileNameUseCase.Base
    ): SetFileNameUseCase

}