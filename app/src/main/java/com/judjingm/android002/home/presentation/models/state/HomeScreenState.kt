package com.judjingm.android002.home.presentation.models.state

import com.judjingm.android002.home.presentation.models.PopularContentUi
import com.judjingm.android002.search.presentation.models.state.SearchScreenState

data class HomeScreenState(
    val isLoading: Boolean = false,
    val pageToLoadMovies: Int = SearchScreenState.FIRST_PAGE,
    val totalPagesMovies: Int = SearchScreenState.DEFAULT_TOTAL_PAGES,
    val pageToLoadTvShows: Int = SearchScreenState.FIRST_PAGE,
    val totalPagesTvShows: Int = SearchScreenState.DEFAULT_TOTAL_PAGES,
    val resultContent: List<PopularContentUi> = emptyList(),
    val errorState: PopularsErrorState = PopularsErrorState.NoError,
    val isPaginationAllowed: Boolean = true,
    val isClickAllowed: Boolean = true

) {
    companion object {
        const val FIRST_PAGE = 1
    }
}