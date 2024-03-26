package com.judjingm.android002.home.presentation.models

data class PopularContentResult(
    val totalPagesMovies: Int = DEFAULTS_PAGE,
    val totalPagesTv: Int = DEFAULTS_PAGE,
    val items: List<PopularContentUi>
) {
    companion object {
        const val DEFAULTS_PAGE = 1
    }
}