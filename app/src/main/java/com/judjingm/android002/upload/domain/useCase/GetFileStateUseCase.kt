package com.judjingm.android002.upload.domain.useCase

import com.judjingm.android002.upload.domain.models.FileUploadState
import com.judjingm.android002.upload.domain.repository.FileUploadRepository
import javax.inject.Inject

interface GetFileStateUseCase {
    suspend operator fun invoke(): FileUploadState

    class Base @Inject constructor(private val repository: FileUploadRepository) :
        GetFileStateUseCase {
        override suspend fun invoke(): FileUploadState {
            return repository.getFileState()
        }
    }
}