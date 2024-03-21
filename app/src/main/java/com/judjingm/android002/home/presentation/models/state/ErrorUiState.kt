package com.judjingm.android002.home.presentation.models.state

import com.judjingm.android002.home.presentation.models.StringVO

sealed interface ErrorUiState {
    data class NoConnection(val message: StringVO) : ErrorUiState
    data class NothingFound(val message: StringVO) : ErrorUiState
    data class CouldNotFetchData(val message: StringVO) : ErrorUiState
    data class UnknownError(val message: StringVO) : ErrorUiState
}