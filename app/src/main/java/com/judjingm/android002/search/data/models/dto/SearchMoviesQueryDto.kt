package com.judjingm.android002.search.data.models.dto

data class SearchMoviesQueryDto(
    val query: String,
    val language: String,
    val page: Int,
    val region: String
)