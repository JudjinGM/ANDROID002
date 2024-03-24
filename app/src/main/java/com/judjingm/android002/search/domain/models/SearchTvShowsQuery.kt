package com.judjingm.android002.search.domain.models

data class SearchTvShowsQuery(
    val query: String,
    val language: String,
    val page: Int,
)
