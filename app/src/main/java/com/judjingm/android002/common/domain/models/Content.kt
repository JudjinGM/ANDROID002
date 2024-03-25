package com.judjingm.android002.common.domain.models

sealed interface Content {
    data class Movie(
        val id: Int?,
        val posterPath: String?,
        val releaseDate: String?,
        val title: String?,
        val voteAverage: Double?
    ) : Content

    data class TVShow(
        val id: Int?,
        val posterPath: String?,
        val firstAirDate: String?,
        val name: String?,
        val voteAverage: Double?
    ) : Content
}