package com.judjingm.android002.upload.data.impl

import android.net.Uri
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.upload.data.api.FileLocalStorage
import com.judjingm.android002.upload.data.api.FileRemoteDataSource
import com.judjingm.android002.upload.domain.models.FileResult
import com.judjingm.android002.upload.domain.repository.FileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val fileLocalStorage: FileLocalStorage,
    private val fileRemoteDataSource: FileRemoteDataSource,
) : FileRepository {

    private val _state: MutableStateFlow<String> = MutableStateFlow(BLANK_STRING)
    val state: MutableStateFlow<String> = _state

    override suspend fun savePdfToPrivateStorage(
        uri: Uri,
        name: String
    ): Resource<FileResult, String> {
        return withContext(Dispatchers.IO) {
            try {
                _state.update { name }
                val response = fileLocalStorage.savePdfToPrivateStorage(uri)
                Resource.Success(
                    FileResult(
                        originalName = state.value,
                        uri = Uri.fromFile(response)
                    )
                )
            } catch (e: Exception) {
                Resource.Error(e.message ?: NO_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun uploadPdfToServer(uri: Uri): Resource<Boolean, String> {
        return try {
            val response = fileRemoteDataSource.uploadFile(uri, state.value)
            fileLocalStorage.deletePdfFromPrivateStorage(uri)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.message ?: NO_ERROR_MESSAGE)
        }
    }

    override fun setFileName(name: String) {
        _state.update { name }
    }

    override fun getFileName(): Flow<String> {
        return state
    }

    companion object {
        private const val BLANK_STRING = ""
        private const val NO_ERROR_MESSAGE = "No error message"
    }

}