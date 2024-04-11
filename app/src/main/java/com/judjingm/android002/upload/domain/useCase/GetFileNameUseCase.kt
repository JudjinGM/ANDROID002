package com.judjingm.android002.upload.domain.useCase

import com.judjingm.android002.upload.domain.repository.FileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetFileNameUseCase {
    operator fun invoke(): Flow<String>

    class Base @Inject constructor(private val repository: FileRepository) : GetFileNameUseCase {
        override fun invoke(): Flow<String> {
            return repository.getFileName()
        }
    }
}