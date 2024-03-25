package com.judjingm.android002.home.presentation.models.state

sealed interface PopularsErrorState {
    data object NoError : PopularsErrorState
    data class NoConnection(val isPagination: Boolean) : PopularsErrorState
    data class ServerError(val isPagination: Boolean) : PopularsErrorState
    data class NotFound(val isPagination: Boolean) : PopularsErrorState
    data class UnknownError(val isPagination: Boolean) : PopularsErrorState
}