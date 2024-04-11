package com.judjingm.android002.upload.presentation.models.state

sealed interface ConfirmUploadErrorState {
    data object NoError : ConfirmUploadErrorState
    data object NoInternet : ConfirmUploadErrorState
    data object NoConnection : ConfirmUploadErrorState
    data object CannotShowFile : ConfirmUploadErrorState
    data class CannotUploadFile(val error: String) : ConfirmUploadErrorState
    data object UnknownError : ConfirmUploadErrorState
}