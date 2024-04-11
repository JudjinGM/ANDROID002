package com.judjingm.android002.upload.presentation.models.state

sealed interface ConfirmUploadEvent {

    data object ConfirmButtonClicked : ConfirmUploadEvent

    data object BackButtonClicked : ConfirmUploadEvent

    data object CancelButtonClicked : ConfirmUploadEvent

    data object ErrorShowingPDf : ConfirmUploadEvent

}