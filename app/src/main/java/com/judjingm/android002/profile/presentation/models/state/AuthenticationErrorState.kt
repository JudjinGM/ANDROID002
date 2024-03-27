package com.judjingm.android002.profile.presentation.models.state

sealed interface AuthenticationErrorState {
    data object NoError : AuthenticationErrorState
    data object NoInternet : AuthenticationErrorState
    data object NoConnection : AuthenticationErrorState
    data object InvalidCredentials : AuthenticationErrorState
    data object CannotProceedTryAgain : AuthenticationErrorState
    data object LoginDeniedByUser : AuthenticationErrorState
    data object UnknownError : AuthenticationErrorState
}