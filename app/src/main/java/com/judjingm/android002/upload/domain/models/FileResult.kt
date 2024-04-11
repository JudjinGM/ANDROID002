package com.judjingm.android002.upload.domain.models

import android.net.Uri

data class FileResult(
    val originalName: String,
    val uri: Uri
)