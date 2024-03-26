package com.judjingm.android002.search.presentation.models.state

sealed interface ProfileEvent {
    data object OnLoginTaped : ProfileEvent
    data object RequestTokenConfirmed : ProfileEvent
    data object RequestTokenDenied : ProfileEvent
    data object OnLogoutTaped : ProfileEvent
}