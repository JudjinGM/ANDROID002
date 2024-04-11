package com.judjingm.android002.upload.presentation.models.state

sealed interface ChooseDocumentNameSideEffects {
    data class SetDocumentName(val name: String) : ChooseDocumentNameSideEffects
    data object NavigateToNextScreen : ChooseDocumentNameSideEffects
}