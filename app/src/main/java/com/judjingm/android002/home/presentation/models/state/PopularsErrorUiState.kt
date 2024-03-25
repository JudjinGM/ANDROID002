package com.judjingm.android002.home.presentation.models.state

import com.judjingm.android002.home.presentation.models.StringVO

sealed interface PopularsErrorUiState {
    data class NoConnection(val message: StringVO) : PopularsErrorUiState
    data class NothingFound(val message: StringVO) : PopularsErrorUiState
    data class CouldNotFetchData(val message: StringVO) : PopularsErrorUiState
    data class UnknownError(val message: StringVO) : PopularsErrorUiState
}