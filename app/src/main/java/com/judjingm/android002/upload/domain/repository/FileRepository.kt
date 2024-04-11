package com.judjingm.android002.upload.domain.repository

import android.net.Uri
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.upload.domain.models.FileResult
import kotlinx.coroutines.flow.Flow

interface FileRepository {
    suspend fun savePdfToPrivateStorage(uri: Uri, name: String): Resource<FileResult, String>
    suspend fun uploadPdfToServer(uri: Uri): Resource<Boolean, String>
    fun setFileName(name: String)
    fun getFileName(): Flow<String>
}