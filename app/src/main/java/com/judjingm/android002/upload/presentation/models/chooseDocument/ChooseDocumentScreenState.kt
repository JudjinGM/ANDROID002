package com.judjingm.android002.upload.presentation.models.chooseDocument

import android.net.Uri

data class ChooseDocumentScreenState(
    val documentUri: Uri = Uri.EMPTY,
    val isDocumentSelectInProcess: Boolean = false,
    val isLoading: Boolean = false,
    val errorState: ChooseDocumentErrorState = ChooseDocumentErrorState.NoError,
)