package com.judjingm.android002.upload.domain.useCase

import android.net.Uri
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.upload.domain.repository.FileRepository
import java.io.File
import javax.inject.Inject

interface SavePdfToPrivateStorageUseCase {
    operator fun invoke(uri: Uri): Resource<File, String>

    class Base @Inject constructor(private val fileRepository: FileRepository) :
        SavePdfToPrivateStorageUseCase {
        override fun invoke(uri: Uri): Resource<File, String> {
            return fileRepository.savePdfToPrivateStorage(uri)
        }
    }

}