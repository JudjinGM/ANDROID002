package com.judjingm.android002.search.domain.models

data class SearchMoviesQuery(
    val query: String,
    val language: String,
    val page: Int,
    val region: String
)
