package com.judjingm.android002.upload.data.impl

import android.net.Uri
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.upload.data.api.FileLocalStorage
import com.judjingm.android002.upload.data.api.FileRemoteDataSource
import com.judjingm.android002.upload.domain.repository.FileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val fileLocalStorage: FileLocalStorage,
    private val fileRemoteDataSource: FileRemoteDataSource,
) : FileRepository {

    private val _state: MutableStateFlow<String> = MutableStateFlow(BLANK_STRING)
    val state: MutableStateFlow<String> = _state

    override fun savePdfToPrivateStorage(uri: Uri): Resource<File, String> {
        return try {
            fileLocalStorage.savePdfToPrivateStorage(uri)
            uri.lastPathSegment?.let {
                _state.update { it }
            }
            fileLocalStorage.savePdfToPrivateStorage(uri)
        } catch (e: Exception) {
            Resource.Error(e.message ?: NO_ERROR_MESSAGE)
        }
    }

    override suspend fun uploadPdfToServer(uri: Uri): Resource<Boolean, String> {
        return try {
            val response = fileRemoteDataSource.uploadFile(uri, state.value)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.message ?: NO_ERROR_MESSAGE)
        }
    }

    companion object {
        private const val BLANK_STRING = ""
        private const val NO_ERROR_MESSAGE = "No error message"
    }
}