package com.judjingm.android002.profile.domain.models

sealed interface Authentication {
    data object Authenticated : Authentication
    data object NotAuthenticated : Authentication
}