package com.judjingm.android002.upload.presentation.models.chooseDocument

sealed interface ChooseDocumentNameSideEffects {
    data object NavigateToNextScreen : ChooseDocumentNameSideEffects
}