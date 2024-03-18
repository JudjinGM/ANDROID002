package com.judjingm.android002.common.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val success: Boolean,
    @SerialName("status_code") val statusCode: Int,
    @SerialName("status_message") val statusMessage: String
)