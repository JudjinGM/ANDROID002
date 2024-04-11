package com.judjingm.android002.upload.domain.models

import android.net.Uri

data class FileUploadState(
    val localFileUri: Uri = Uri.EMPTY,
    val fileName: String = BLANC_NAME
) {

    companion object {
        private const val BLANC_NAME = ""
    }
}