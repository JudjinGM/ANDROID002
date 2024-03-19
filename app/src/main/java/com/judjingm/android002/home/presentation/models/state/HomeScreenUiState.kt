package com.judjingm.android002.home.presentation.models.state

import com.judjingm.android002.home.presentation.models.PopularContentUi

data class HomeScreenUiState(
    val state: UiState
) {
    sealed interface UiState {
        fun handleState(handler: StateHandler)

        data object Loading : UiState {
            override fun handleState(handler: StateHandler) {
                handler.handleLoading()
            }
        }

        sealed class Success(
            open val content: List<PopularContentUi> = emptyList(),
        ) : UiState {

            data object Empty : Success()
            data class Content(override val content: List<PopularContentUi>) : Success(content)

            override fun handleState(handler: StateHandler) {
                handler.handleSuccess(content)
            }
        }

        data class Error(val error: UiErrorsState) : UiState {
            override fun handleState(handler: StateHandler) {
                handler.handleError(error)
            }
        }
    }

    interface StateHandler {
        fun handleLoading()

        fun handleSuccess(
            content: List<PopularContentUi>,
        )

        fun handleError(error: UiErrorsState)
    }
}