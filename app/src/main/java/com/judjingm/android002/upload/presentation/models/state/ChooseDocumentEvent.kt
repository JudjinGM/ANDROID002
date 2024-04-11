package com.judjingm.android002.upload.presentation.models.state

import android.net.Uri

sealed interface ChooseDocumentEvent {
    data object ChooseDocumentClicked : ChooseDocumentEvent
    data class DocumentSelected(val uri: Uri, val name: String) : ChooseDocumentEvent
    data object ProceedNextClicked : ChooseDocumentEvent
}