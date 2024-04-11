package com.judjingm.android002.upload.data.impl

import android.net.Uri
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.upload.data.api.FileLocalStorage
import com.judjingm.android002.upload.data.api.FileRemoteDataSource
import com.judjingm.android002.upload.domain.models.FileResult
import com.judjingm.android002.upload.domain.models.FileUploadState
import com.judjingm.android002.upload.domain.repository.FileUploadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FileUploadUploadRepositoryImpl @Inject constructor(
    private val fileLocalStorage: FileLocalStorage,
    private val fileRemoteDataSource: FileRemoteDataSource,
) : FileUploadRepository {

    private val _state: MutableStateFlow<FileUploadState> = MutableStateFlow(FileUploadState())
    val state: MutableStateFlow<FileUploadState> = _state

    override suspend fun savePdfToPrivateStorage(
        uri: Uri,
        name: String
    ): Resource<FileResult, String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = fileLocalStorage.savePdfToPrivateStorage(uri)

                _state.update {
                    it.copy(
                        fileName = name,
                        localFileUri = Uri.fromFile(response)
                    )
                }

                Resource.Success(
                    FileResult(
                        originalName = name,
                        uri = Uri.fromFile(response)
                    )
                )
            } catch (e: Exception) {
                Resource.Error(e.message ?: NO_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun uploadPdfToServer(): Resource<Boolean, String> {
        return try {
            val response =
                fileRemoteDataSource.uploadFile(state.value.localFileUri, state.value.fileName)
            fileLocalStorage.deletePdfFromPrivateStorage(state.value.localFileUri)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.message ?: NO_ERROR_MESSAGE)
        }
    }

    override fun setFileName(name: String) {
        _state.update { it.copy(fileName = name) }
    }

    override suspend fun getFileState(): FileUploadState {
        return state.value
    }

    companion object {
        private const val NO_ERROR_MESSAGE = "No error message"
    }

}