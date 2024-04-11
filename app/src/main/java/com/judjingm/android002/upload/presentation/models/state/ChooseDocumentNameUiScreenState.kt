package com.judjingm.android002.upload.presentation.models.state

sealed interface ChooseDocumentNameUiScreenState {
    data object Loading : ChooseDocumentNameUiScreenState
    data object Success : ChooseDocumentNameUiScreenState

}