package com.judjingm.android002.profile.data.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDetailsResponse(
    val id: Int,
    @SerialName("username") val userName: String
)