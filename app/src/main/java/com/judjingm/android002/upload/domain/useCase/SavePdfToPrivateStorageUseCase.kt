package com.judjingm.android002.upload.domain.useCase

import android.net.Uri
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.upload.domain.models.FileResult
import com.judjingm.android002.upload.domain.repository.FileUploadRepository
import javax.inject.Inject

interface SavePdfToPrivateStorageUseCase {
    suspend operator fun invoke(uri: Uri, name: String): Resource<FileResult, String>

    class Base @Inject constructor(private val fileUploadRepository: FileUploadRepository) :
        SavePdfToPrivateStorageUseCase {
        override suspend fun invoke(uri: Uri, name: String): Resource<FileResult, String> {
            return fileUploadRepository.savePdfToPrivateStorage(uri, name)
        }
    }

}