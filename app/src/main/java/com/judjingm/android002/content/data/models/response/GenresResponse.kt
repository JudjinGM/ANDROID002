package com.judjingm.android002.content.data.models.response

import kotlinx.serialization.Serializable

@Serializable
data class GenresResponse(
    val id: Int,
    val name: String
)