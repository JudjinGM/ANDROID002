package com.judjingm.android002.search.presentation.models

data class SearchContentResult(
    val totalPagesMovies: Int,
    val totalPagesTv: Int,
    val items: List<SearchContentUiItem>
)