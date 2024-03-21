package com.judjingm.android002.home.presentation.models

data class PopularContentUi(
    val id: Int = DEFAULT_ID,
    val posterPath: String = EMPTY_STRING,
    val releaseDate: String = EMPTY_STRING,
    val title: String = EMPTY_STRING,
    val type: ContentType = ContentType.UNKNOWN
) {
    companion object {
        const val DEFAULT_ID = -1
        const val EMPTY_STRING = ""
    }
}
