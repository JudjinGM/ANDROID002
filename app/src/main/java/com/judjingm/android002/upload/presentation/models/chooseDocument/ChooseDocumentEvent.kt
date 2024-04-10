package com.judjingm.android002.upload.presentation.models.chooseDocument

import android.net.Uri

sealed interface ChooseDocumentEvent {
    data object ChooseDocumentClicked : ChooseDocumentEvent
    data class DocumentSelected(val uri: Uri) : ChooseDocumentEvent
    data object ProceedNextClicked : ChooseDocumentEvent
}