package com.judjingm.android002.home.data.models.request

import kotlinx.serialization.Serializable

@Serializable
data class PopularTVShowsRequest(
    val page: Int,
    val language: String
)