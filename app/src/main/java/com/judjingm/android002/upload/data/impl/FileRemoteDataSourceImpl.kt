package com.judjingm.android002.upload.data.impl

import android.net.Uri
import app.cashadvisor.common.utill.extensions.logDebugMessage
import com.judjingm.android002.common.utill.exceptions.NetworkException
import com.judjingm.android002.upload.data.NetworkToUploadFileExceptionMapper
import com.judjingm.android002.upload.data.api.FileApiService
import com.judjingm.android002.upload.data.api.FileRemoteDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class FileRemoteDataSourceImpl @Inject constructor(
    private val fileService: FileApiService,
    private val exceptionMapper: NetworkToUploadFileExceptionMapper
) :
    FileRemoteDataSource {
    override suspend fun uploadFile(uri: Uri, fileName: String) {
        return try {
            val file = File(uri.path)
            val name = fileName.ifBlank { file.name }
            fileService.uploadDocument(
                MultipartBody.Part.createFormData(
                    FILE,
                    name,
                    file.asRequestBody()
                )
            )
        } catch (exception: NetworkException) {
            logDebugMessage(exception.message)
            throw exceptionMapper.handleException(exception)
        }
    }

    companion object {
        private const val FILE = "file"

    }
}