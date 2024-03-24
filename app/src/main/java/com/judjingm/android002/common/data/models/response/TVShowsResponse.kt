package com.judjingm.android002.common.data.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TVShowsResponse(
    val page: Int?,
    val results: List<TVShowResults>,
    @SerialName("total_pages") val totalPages: Int?,
    @SerialName("total_results") val totalResults: Int?
)