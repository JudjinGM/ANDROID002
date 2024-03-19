package com.judjingm.android002.home.presentation.models.state

sealed interface UiErrorsState {
    data object NoConnection : UiErrorsState
    data object NothingFound : UiErrorsState
    data object CouldNotFetchData : UiErrorsState
    data object UnknownError : UiErrorsState
}