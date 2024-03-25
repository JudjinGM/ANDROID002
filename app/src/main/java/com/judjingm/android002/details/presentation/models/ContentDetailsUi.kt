package com.judjingm.android002.details.presentation.models

data class ContentDetailsUi(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val runtime: String,
    val genres: String,
    val posterPath: String,
    val overview: String,
    val cast: String
)