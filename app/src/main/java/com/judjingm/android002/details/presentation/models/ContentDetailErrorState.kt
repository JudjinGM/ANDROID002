package com.judjingm.android002.details.presentation.models

import com.judjingm.android002.home.presentation.models.StringVO

sealed class ContentDetailErrorState {
    data class NoConnection(val message: StringVO) : ContentDetailErrorState()
    data class ServerError(val message: StringVO) : ContentDetailErrorState()
    data class UnknownError(val message: StringVO) : ContentDetailErrorState()

}