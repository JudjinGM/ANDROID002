package com.judjingm.android002.upload.presentation.models.state

sealed interface ChooseDocumentNameEvent {
    data class OnDocumentNameChanged(val name: String) : ChooseDocumentNameEvent
    data object ProceedNextClicked : ChooseDocumentNameEvent
}