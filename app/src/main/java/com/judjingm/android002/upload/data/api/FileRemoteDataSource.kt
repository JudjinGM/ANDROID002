package com.judjingm.android002.upload.data.api

import android.net.Uri

interface FileRemoteDataSource {
    suspend fun uploadFile(uri: Uri, fileName: String)
}