package com.judjingm.android002.upload.presentation.models.chooseDocument

sealed interface ChooseDocumentNameEvent {
    data class OnDocumentNameChanged(val name: String) : ChooseDocumentNameEvent
    data object ProceedNextClicked : ChooseDocumentNameEvent
}