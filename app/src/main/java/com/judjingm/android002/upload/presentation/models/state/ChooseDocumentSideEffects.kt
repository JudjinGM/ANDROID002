package com.judjingm.android002.upload.presentation.models.state

import com.judjingm.android002.home.presentation.models.StringVO

sealed interface ChooseDocumentSideEffects {

    data class ShowMessage(val message: StringVO) : ChooseDocumentSideEffects

    data object OpenDocumentPicker : ChooseDocumentSideEffects

    data object NavigateToNextScreen : ChooseDocumentSideEffects
}