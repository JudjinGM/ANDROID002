package com.judjingm.android002.details.presentation.models

sealed interface ContentDetailSideEffect {
    data object NavigateBack : ContentDetailSideEffect
}