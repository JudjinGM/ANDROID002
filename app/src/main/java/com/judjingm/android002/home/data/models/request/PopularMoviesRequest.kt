package com.judjingm.android002.home.data.models.request

data class PopularMoviesRequest(
    val language: String,
    val page: Int,
    val region: String
)