package com.judjingm.android002.upload.data.api

import android.net.Uri
import com.judjingm.android002.common.utill.Resource
import java.io.File

interface FileLocalStorage {
    fun savePdfToPrivateStorage(uri: Uri): Resource<File, String>
    fun deletePdfFromPrivateStorage(uri: Uri)
}