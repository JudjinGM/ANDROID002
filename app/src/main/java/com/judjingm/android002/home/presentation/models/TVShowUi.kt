package com.judjingm.android002.home.presentation.models

data class TVShowUi(
    val id: Int = DEFAULT_ID,
    val posterPath: String = EMPTY_STRING,
    val firstAirDate: String = EMPTY_STRING,
    val name: String = EMPTY_STRING
) {
    companion object {
        const val DEFAULT_ID = -1
        const val EMPTY_STRING = ""
    }
}
