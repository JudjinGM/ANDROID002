package com.judjingm.android002.home.presentation.models.state

import com.judjingm.android002.home.presentation.models.PopularContentUi

sealed interface HomeScreenUiState {
    fun handleState(handler: StateHandler)

    data class Loading(private val isPagination: Boolean = false) : HomeScreenUiState {

        override fun handleState(handler: StateHandler) {
            handler.handleLoading(isPagination)
        }
    }

    data class Success(private val content: List<PopularContentUi> = emptyList()) :
        HomeScreenUiState {
        override fun handleState(handler: StateHandler) {
            handler.handleSuccess(content)
        }
    }

    data class Error(private val error: ErrorUiState) : HomeScreenUiState {
        override fun handleState(handler: StateHandler) {
            handler.handleError(error)
        }
    }

    interface StateHandler {
        fun handleLoading(isPagination: Boolean)

        fun handleSuccess(content: List<PopularContentUi>)

        fun handleError(error: ErrorUiState)
    }
}