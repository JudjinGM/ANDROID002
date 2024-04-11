package com.judjingm.android002.upload.presentation.models.chooseDocument

sealed interface ChooseDocumentNameUiScreenState {
    data object Loading : ChooseDocumentNameUiScreenState
    data class Success(val name: String) : ChooseDocumentNameUiScreenState

}