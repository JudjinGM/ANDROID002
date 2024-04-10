package com.judjingm.android002.upload.presentation.models.chooseDocument

import android.net.Uri

sealed interface ChooseDocumentUiScreenState {
    data object Initial : ChooseDocumentUiScreenState
    data object Loading : ChooseDocumentUiScreenState
    data class Success(val uri: Uri) : ChooseDocumentUiScreenState
}