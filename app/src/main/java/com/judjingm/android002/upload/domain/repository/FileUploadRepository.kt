package com.judjingm.android002.upload.domain.repository

import android.net.Uri
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.upload.domain.models.FileResult
import com.judjingm.android002.upload.domain.models.FileUploadState
import kotlinx.coroutines.flow.Flow

interface FileUploadRepository {
    suspend fun savePdfToPrivateStorage(uri: Uri, name: String): Resource<FileResult, String>
    suspend fun uploadPdfToServer(): Flow<Resource<Boolean, String>>
    fun setFileName(name: String)
    suspend fun getFileState(): FileUploadState
}