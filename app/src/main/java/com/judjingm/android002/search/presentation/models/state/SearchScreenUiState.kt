package com.judjingm.android002.search.presentation.models.state

import com.judjingm.android002.search.presentation.models.SearchContentUiItem

sealed interface SearchScreenUiState {
    fun handleState(handler: StateHandler)

    data class Loading(private val isPagination: Boolean = false) : SearchScreenUiState {

        override fun handleState(handler: StateHandler) {
            handler.handleLoading(isPagination)
        }
    }

    data class Success(private val content: List<SearchContentUiItem> = emptyList()) :
        SearchScreenUiState {
        override fun handleState(handler: StateHandler) {
            handler.handleSuccess(content)
        }
    }

    data class Error(private val error: SearchErrorUiState) : SearchScreenUiState {
        override fun handleState(handler: StateHandler) {
            handler.handleError(error)
        }
    }

    interface StateHandler {
        fun handleLoading(isPagination: Boolean)
        fun handleSuccess(content: List<SearchContentUiItem>)
        fun handleError(error: SearchErrorUiState)
    }
}