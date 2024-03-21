package com.judjingm.android002.content.data.models.dto

data class TvShowDetailDto(
    val id: Int,
    val name: String,
    val genres: List<GenreDto>,
    val firstAirDate: String,
    val overview: String,
    val posterPath: String,
    val episodes: Int,
    val seasons: Int,
)