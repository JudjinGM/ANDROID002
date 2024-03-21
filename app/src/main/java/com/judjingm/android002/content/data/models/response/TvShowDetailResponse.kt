package com.judjingm.android002.content.data.models.response

import kotlinx.serialization.SerialName

data class TvShowDetailResponse(
    val id: Int,
    val name: String,
    val genres: List<GenresResponse>,
    @SerialName("first_air_date") val firstAirDate: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String,
    @SerialName("number_of_episodes") val episodes: Int,
    @SerialName("number_of_seasons") val seasons: Int,
)