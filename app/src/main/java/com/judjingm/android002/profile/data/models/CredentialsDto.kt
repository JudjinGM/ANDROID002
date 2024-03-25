package com.judjingm.android002.profile.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CredentialsDto(
    val sessionId: String,
) : Parcelable