package com.judjingm.android002.upload.presentation.models.state

import android.net.Uri

sealed interface ConfirmUploadUiScreenState {
    data object Loading : ConfirmUploadUiScreenState
    data class ReadyToUplLoad(val name: String, val uri: Uri) : ConfirmUploadUiScreenState
    data object Uploading : ConfirmUploadUiScreenState
    data object Success : ConfirmUploadUiScreenState
    data class Error(val errorState: ConfirmUploadUiErrorState) : ConfirmUploadUiScreenState
}