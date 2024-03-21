package com.judjingm.android002.home.presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ContentType : Parcelable {
    MOVIE,
    TVSHOW,
    UNKNOWN
}