package com.judjingm.android002.upload.domain.useCase

import com.judjingm.android002.upload.domain.repository.FileUploadRepository
import javax.inject.Inject

interface SetFileNameUseCase {
    operator fun invoke(fileName: String)

    class Base @Inject constructor(private val repository: FileUploadRepository) :
        SetFileNameUseCase {
        override fun invoke(fileName: String) {
            repository.setFileName(fileName)
        }
    }
}