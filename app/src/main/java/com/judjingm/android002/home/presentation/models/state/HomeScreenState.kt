package com.judjingm.android002.home.presentation.models.state

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.home.presentation.models.PopularContentUi

data class HomeScreenState(
    val isLoading: Boolean = false,
    val pageToLoad: Int = FIRST_PAGE,
    val popularContent: PagedList<PopularContentUi> = PagedList(),
    val errorState: PopularsErrorState = PopularsErrorState.NoError,
    val isPaginationAllowed: Boolean = true,
    val isClickAllowed: Boolean = true

) {
    companion object {
        const val FIRST_PAGE = 1
    }
}