package com.judjingm.android002.content.presentation.models

sealed interface ContentDetailEvent {
    data object OnBackClicked : ContentDetailEvent
}