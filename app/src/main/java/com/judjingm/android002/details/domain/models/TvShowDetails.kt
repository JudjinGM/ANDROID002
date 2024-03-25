package com.judjingm.android002.details.domain.models

data class TvShowDetails(
    val id: Int,
    val name: String,
    val genres: List<Genre>,
    val firstAirDate: String,
    val overview: String,
    val posterPath: String,
    val episodes: Int,
    val seasons: Int,
)