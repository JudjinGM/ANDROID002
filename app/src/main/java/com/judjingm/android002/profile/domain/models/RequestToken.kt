package com.judjingm.android002.profile.domain.models

data class RequestToken(
    val success: Boolean,
    val expiresAt: String,
    val requestToken: String
)