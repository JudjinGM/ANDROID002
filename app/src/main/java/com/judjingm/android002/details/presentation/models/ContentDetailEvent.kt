package com.judjingm.android002.details.presentation.models

sealed interface ContentDetailEvent {
    data object OnBackClicked : ContentDetailEvent
}