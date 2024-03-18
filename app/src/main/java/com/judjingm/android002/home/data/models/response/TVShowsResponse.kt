package com.judjingm.android002.home.data.models.response

import kotlinx.serialization.SerialName

data class TVShowsResponse(
    val page: Int?,
    val results: List<TVShowsResults>,
    @SerialName("total_pages") val totalPages: Int?,
    @SerialName("total_results") val totalResults: Int?
)