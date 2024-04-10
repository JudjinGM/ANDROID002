package com.judjingm.android002.upload.domain.useCase

import android.net.Uri
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.upload.domain.repository.FileRepository
import javax.inject.Inject

interface UploadPdfToServerUseCase {
    suspend operator fun invoke(uri: Uri): Resource<Boolean, String>
    class Base @Inject constructor(private val fileRepository: FileRepository) :
        UploadPdfToServerUseCase {
        override suspend fun invoke(uri: Uri): Resource<Boolean, String> {
            return fileRepository.uploadPdfToServer(uri)
        }
    }

}