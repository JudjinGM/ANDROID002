package com.judjingm.android002.details.data.models.dto

data class TvShowDetailsDto(
    val id: Int?,
    val name: String?,
    val genres: List<GenreDto>?,
    val firstAirDate: String?,
    val overview: String?,
    val posterPath: String?,
    val episodes: Int?,
    val seasons: Int?,
)