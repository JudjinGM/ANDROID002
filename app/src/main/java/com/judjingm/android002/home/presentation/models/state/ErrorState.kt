package com.judjingm.android002.home.presentation.models.state

sealed interface ErrorState {
    data object NoError : ErrorState
    data class NoConnection(val isPagination: Boolean) : ErrorState
    data class ServerError(val isPagination: Boolean) : ErrorState
    data class NotFound(val isPagination: Boolean) : ErrorState
    data class UnknownError(val isPagination: Boolean) : ErrorState
}