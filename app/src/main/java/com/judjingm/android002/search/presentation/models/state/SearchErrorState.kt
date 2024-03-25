package com.judjingm.android002.search.presentation.models.state

sealed interface SearchErrorState {
    data object NoError : SearchErrorState
    data class NoConnection(val isPagination: Boolean) : SearchErrorState
    data class ServerError(val isPagination: Boolean) : SearchErrorState
    data class NotFound(val isPagination: Boolean) : SearchErrorState
    data class UnknownError(val isPagination: Boolean) : SearchErrorState
}