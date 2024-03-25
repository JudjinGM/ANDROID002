package com.judjingm.android002.details.domain.models

data class MovieDetails(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val runtime: Int,
    val genres: List<Genre>,
    val posterPath: String,
    val overview: String,
)