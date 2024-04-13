package com.judjingm.android002.upload.domain.useCase

import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.upload.domain.repository.FileUploadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UploadPdfToServerUseCase {
    suspend operator fun invoke(): Flow<Resource<Boolean, String>>
    class Base @Inject constructor(private val fileUploadRepository: FileUploadRepository) :
        UploadPdfToServerUseCase {
        override suspend fun invoke(): Flow<Resource<Boolean, String>> {
            return fileUploadRepository.uploadPdfToServer()
        }
    }
}