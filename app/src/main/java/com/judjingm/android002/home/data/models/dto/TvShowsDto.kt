package com.judjingm.android002.home.data.models.dto

data class TvShowsDto(
    val page: Int?,
    val results: List<TvShowDto>,
    val totalPages: Int?,
    val totalResults: Int?
)