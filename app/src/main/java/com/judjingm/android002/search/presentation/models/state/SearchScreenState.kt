package com.judjingm.android002.search.presentation.models.state

import com.judjingm.android002.search.presentation.models.SearchContentUiItem

data class SearchScreenState(
    val query: String = BLANC_QUERY,
    val isLoading: Boolean = false,
    val pageToLoadMovies: Int = FIRST_PAGE,
    val totalPagesMovies: Int = DEFAULT_TOTAL_PAGES,
    val pageToLoadTvShows: Int = FIRST_PAGE,
    val totalPagesTvShows: Int = DEFAULT_TOTAL_PAGES,
    val resultContent: List<SearchContentUiItem> = emptyList(),
    val errorState: SearchErrorState = SearchErrorState.NoError,
    val isPaginationAllowed: Boolean = true,
    val isClickAllowed: Boolean = true
) {
    companion object {
        const val DEFAULT_TOTAL_PAGES = 1
        const val FIRST_PAGE = 1
        const val BLANC_QUERY = ""
    }
}