package com.judjingm.android002.home.presentation.models.state

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.home.presentation.models.PopularContentUi
import com.judjingm.android002.home.presentation.models.StringVO

data class HomeScreenState(
    val currentPage: Int = FIRST_PAGE,
    val popularContent: PagedList<PopularContentUi> = PagedList(),
    val isErrorFetchingMovie: Boolean = false,
    val isErrorFetchingTVShows: Boolean = false,
    val errorMessage: StringVO = StringVO.Plain(EMPTY_STRING),
) {
    companion object {
        const val FIRST_PAGE = 1
        const val EMPTY_STRING = ""
    }
}