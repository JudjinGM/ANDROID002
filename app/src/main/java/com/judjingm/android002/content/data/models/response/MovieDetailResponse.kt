package com.judjingm.android002.content.data.models.response

import kotlinx.serialization.SerialName

data class MovieDetailResponse(
    val id: Int,
    val title: String,
    @SerialName("release_date") val releaseDate: String,
    val runtime: Int,
    val genres: List<GenresResponse>,
    @SerialName("poster_path") val posterPath: String,
    val overview: String,
)