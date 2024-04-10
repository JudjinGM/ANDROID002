package com.judjingm.android002.upload.data.impl

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.upload.data.api.FileLocalStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class FileLocalStorageImpl @Inject constructor(@ApplicationContext private val context: Context) :
    FileLocalStorage {
    override fun savePdfToPrivateStorage(uri: Uri): Resource<File, String> {
        val fileName = uri.lastPathSegment ?: DEFAULT_PDF_FILE_NAME
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), DIRECTORY_PATH)

        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, fileName)

        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Resource.Success(file)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error of saving file")
        }
    }

    override fun deletePdfFromPrivateStorage(uri: Uri) {
        uri.path?.let {
            File(it).delete()
        }
    }

    companion object {
        const val DEFAULT_PDF_FILE_NAME = "new_file.pdf"
        const val DIRECTORY_PATH = "pdf_files"
    }
}