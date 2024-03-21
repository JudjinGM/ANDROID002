package com.judjingm.android002.content.data.models.response

import kotlinx.serialization.Serializable

@Serializable
class CreditsResponse(
    val id: Int,
    val cast: List<CastResponse>,
)