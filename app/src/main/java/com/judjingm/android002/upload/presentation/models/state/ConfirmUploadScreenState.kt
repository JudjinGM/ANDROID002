package com.judjingm.android002.upload.presentation.models.state

import android.net.Uri

data class ConfirmUploadScreenState(
    val documentName: String = "",
    val documentUri: Uri = Uri.EMPTY,
    val errorState: ConfirmUploadErrorState = ConfirmUploadErrorState.NoError,
)