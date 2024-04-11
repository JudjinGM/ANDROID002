package com.judjingm.android002.upload.presentation.models.state

import android.net.Uri
import com.judjingm.android002.home.presentation.models.StringVO

sealed interface ConfirmUploadUiScreenState {
    data object Loading : ConfirmUploadUiScreenState
    data class Success(val name: String, val uri: Uri) : ConfirmUploadUiScreenState
    data class Error(val message: StringVO) : ConfirmUploadUiScreenState
}