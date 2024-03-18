package com.judjingm.android002.home.data.models.dto

data class MoviesDto (
    val page: Int?,
    val results: List<MovieDto>,
    val totalPages: Int?,
    val totalResults: Int?
)