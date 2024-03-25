package com.judjingm.android002.search.presentation.models.state

import com.judjingm.android002.home.presentation.models.ContentType

sealed interface SearchScreenEvent {
    data class OnSearchQueryChanged(val query: String) : SearchScreenEvent
    data object PaginationTriggered : SearchScreenEvent
    data class OnContentClicked(val contentId: Int, val contentType: ContentType) :
        SearchScreenEvent

    data object OnSearchQueryCleared : SearchScreenEvent

}