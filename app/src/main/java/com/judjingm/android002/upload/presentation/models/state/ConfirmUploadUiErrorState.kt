package com.judjingm.android002.upload.presentation.models.state

import com.judjingm.android002.home.presentation.models.StringVO

sealed class ConfirmUploadUiErrorState(open val message: StringVO) {
    data class FileLoadError(override val message: StringVO) : ConfirmUploadUiErrorState(message)
    data class FileUploadError(override val message: StringVO) : ConfirmUploadUiErrorState(message)

}