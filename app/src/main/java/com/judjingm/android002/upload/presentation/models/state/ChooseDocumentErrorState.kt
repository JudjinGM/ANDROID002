package com.judjingm.android002.upload.presentation.models.state

sealed interface ChooseDocumentErrorState {
    data object NoError : ChooseDocumentErrorState
    data class CannotChooseDocumentFile(val error: String) : ChooseDocumentErrorState
    data object UnknownError : ChooseDocumentErrorState
}