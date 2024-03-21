package com.judjingm.android002.content.presentation.models

sealed interface ContentDetailSideEffect {
    data object NavigateBack : ContentDetailSideEffect
}