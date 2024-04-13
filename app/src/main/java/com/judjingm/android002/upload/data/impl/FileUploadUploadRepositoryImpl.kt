package com.judjingm.android002.upload.data.impl

import android.content.Context
import android.net.Uri
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.upload.data.UploadWorker
import com.judjingm.android002.upload.data.api.FileLocalStorage
import com.judjingm.android002.upload.domain.models.FileResult
import com.judjingm.android002.upload.domain.models.FileUploadState
import com.judjingm.android002.upload.domain.repository.FileUploadRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FileUploadUploadRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fileLocalStorage: FileLocalStorage
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

    override suspend fun uploadPdfToServer(): Flow<Resource<Boolean, String>> {
        val state: MutableStateFlow<Resource<Boolean, String>> =
            MutableStateFlow(Resource.Success(false))

        val inputData = getInputData()
        val workRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInputData(inputData)
            .addTag(TAG)
            .build()
        val workManager = WorkManager.getInstance(context)
        workManager.enqueue(workRequest)
        workManager.getWorkInfoByIdLiveData(workRequest.id).observeForever { workInfo ->
            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                state.update {
                    Resource.Success(true)
                }
            } else if (workInfo.state == WorkInfo.State.FAILED) {
                state.update {
                    val data = workInfo.outputData
                    val error = data.getString(ERROR_MESSAGE) ?: NO_ERROR_MESSAGE
                    Resource.Error(error)
                }
            }
        }
        return state
    }

    override fun setFileName(name: String) {
        _state.update { it.copy(fileName = name) }
    }

    override suspend fun getFileState(): FileUploadState {
        return state.value
    }

    private fun getInputData(): Data {
        val dataBuilder = Data.Builder()
        dataBuilder.putString(URI, state.value.localFileUri.toString()).build()
        dataBuilder.putString(FILE_NAME, state.value.fileName).build()
        return dataBuilder.build()
    }

    companion object {
        private const val NO_ERROR_MESSAGE = "No error message"
        private const val URI = "uri"
        private const val FILE_NAME = "file_name"
        private const val TAG = "UploadWorker"
        private const val ERROR_MESSAGE = "error_message"
    }

}

