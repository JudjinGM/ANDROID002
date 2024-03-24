package com.judjingm.android002.common.data.models

class MovieDto(
    val adult: Boolean?,
    val backdropPath: String?,
    val genreIds: ArrayList<Int>,
    val id: Int?,
    val originalLanguage: String?,
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double?,
    val posterPath: String?,
    val releaseDate: String?,
    val title: String?,
    val video: Boolean?,
    val voteAverage: Double?,
    val voteCount: Int?
)