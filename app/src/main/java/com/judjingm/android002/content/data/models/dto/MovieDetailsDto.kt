package com.judjingm.android002.content.data.models.dto

data class MovieDetailsDto(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val runtime: Int,
    val genres: List<GenreDto>,
    val posterPath: String,
    val overview: String,
)