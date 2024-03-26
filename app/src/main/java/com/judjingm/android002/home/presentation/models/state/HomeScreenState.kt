package com.judjingm.android002.home.presentation.models.state

import com.judjingm.android002.home.presentation.models.PopularContentUi

data class HomeScreenState(
    val isLoading: Boolean = false,
    val pageToLoadMovies: Int = FIRST_PAGE,
    val totalPagesMovies: Int = DEFAULT_TOTAL_PAGES,
    val pageToLoadTvShows: Int = FIRST_PAGE,
    val totalPagesTvShows: Int = DEFAULT_TOTAL_PAGES,
    val resultContent: List<PopularContentUi> = emptyList(),
    val errorState: PopularsErrorState = PopularsErrorState.NoError,
    val isPaginationAllowed: Boolean = true,
    val isClickAllowed: Boolean = true

) {
    companion object {
        const val DEFAULT_TOTAL_PAGES = 1
        const val FIRST_PAGE = 1
    }
}