package com.judjingm.android002.profile.data.models

data class RequestTokenDto(
    val success: Boolean,
    val expiresAt: String,
    val requestToken: String
)