package com.judjingm.android002.upload.presentation.models.chooseDocument

sealed interface ChooseDocumentErrorState {
    data object NoError : ChooseDocumentErrorState
    data object NoInternet : ChooseDocumentErrorState
    data object NoConnection : ChooseDocumentErrorState

    data class CannotChooseDocumentFile(val error: String) : ChooseDocumentErrorState

    data object UnknownError : ChooseDocumentErrorState
}