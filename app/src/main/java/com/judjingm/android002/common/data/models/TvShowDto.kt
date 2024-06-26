package com.judjingm.android002.common.data.models

data class TvShowDto(
    val adult: Boolean?,
    val backdropPath: String?,
    val genreIds: List<Int>,
    val id: Int?,
    val originCountry: List<String>,
    val originalLanguage: String?,
    val originalName: String?,
    val overview: String?,
    val popularity: Double?,
    val posterPath: String?,
    val firstAirDate: String?,
    val name: String?,
    val voteAverage: Double?,
    val voteCount: Int?
)