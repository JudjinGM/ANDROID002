package com.judjingm.android002.upload.data.api

import android.net.Uri
import java.io.File

interface FileLocalStorage {
    fun savePdfToPrivateStorage(uri: Uri): File
    fun deletePdfFromPrivateStorage(uri: Uri)
}