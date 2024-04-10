package com.judjingm.android002.upload.domain.repository

import android.net.Uri
import com.judjingm.android002.common.utill.Resource
import java.io.File

interface FileRepository {
    fun savePdfToPrivateStorage(uri: Uri): Resource<File, String>
    suspend fun uploadPdfToServer(uri: Uri): Resource<Boolean, String>
}