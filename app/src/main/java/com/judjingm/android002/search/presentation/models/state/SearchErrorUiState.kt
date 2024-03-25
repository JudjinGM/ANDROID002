package com.judjingm.android002.search.presentation.models.state

import com.judjingm.android002.home.presentation.models.StringVO

sealed interface SearchErrorUiState {
    data class NoConnection(val message: StringVO) : SearchErrorUiState
    data class NothingFound(val message: StringVO) : SearchErrorUiState
    data class CouldNotFetchData(val message: StringVO) : SearchErrorUiState
    data class UnknownError(val message: StringVO) : SearchErrorUiState
}