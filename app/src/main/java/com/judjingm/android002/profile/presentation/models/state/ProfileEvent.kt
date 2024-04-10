package com.judjingm.android002.profile.presentation.models.state

sealed interface ProfileEvent {
    data object OnLoginTaped : ProfileEvent
    data object RequestTokenConfirmed : ProfileEvent
    data object RequestTokenDenied : ProfileEvent
    data object CloseButtonClicked : ProfileEvent
    data object OnLogoutTaped : ProfileEvent
}