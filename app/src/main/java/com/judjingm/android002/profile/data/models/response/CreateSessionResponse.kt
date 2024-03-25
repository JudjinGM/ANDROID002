package com.judjingm.android002.profile.data.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSessionResponse(
    val success: Boolean,
    @SerialName("session_id") val sessionId: String
)
