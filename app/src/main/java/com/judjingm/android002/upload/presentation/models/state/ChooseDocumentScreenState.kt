package com.judjingm.android002.upload.presentation.models.state

import android.net.Uri

data class ChooseDocumentScreenState(
    val documentName: String = BLANC_STRING,
    val documentUri: Uri = Uri.EMPTY,
    val isDocumentSelectInProcess: Boolean = false,
    val isLoading: Boolean = false,
    val errorState: ChooseDocumentErrorState = ChooseDocumentErrorState.NoError,
) {
    companion object {
        private const val BLANC_STRING = ""
    }
}