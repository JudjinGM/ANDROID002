package com.judjingm.android002.home.domain.models

data class PopularMoviesQuery(
    val language: String,
    val page: Int,
    val region: String
)
