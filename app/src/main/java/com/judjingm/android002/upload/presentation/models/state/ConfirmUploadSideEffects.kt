package com.judjingm.android002.upload.presentation.models.state

import com.judjingm.android002.home.presentation.models.StringVO

sealed interface ConfirmUploadSideEffects {
    data object NavigateBack : ConfirmUploadSideEffects
    data object CancelUploadConfirmation : ConfirmUploadSideEffects
    data class ShowMessage(val message: StringVO) : ConfirmUploadSideEffects
}