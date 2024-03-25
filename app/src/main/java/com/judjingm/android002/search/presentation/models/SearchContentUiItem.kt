package com.judjingm.android002.search.presentation.models

import com.judjingm.android002.home.presentation.models.ContentType
import com.judjingm.android002.home.presentation.models.StringVO

sealed class SearchContentUiItem(
    open var id: Int,
    open val type: ContentType
) {
    data class TitleUiItem(
        override var id: Int = DEFAULT_ID,
        val title: StringVO
    ) : SearchContentUiItem(id, type = ContentType.UNKNOWN)

    data class MovieUiItem(
        override var id: Int,
        val posterPath: String,
        val releaseDate: String,
        val title: String,
        val averageVote: String,
        override val type: ContentType
    ) : SearchContentUiItem(id, type = type)

    data class TvShowUiItem(
        override var id: Int,
        val posterPath: String,
        val firstAirDate: String,
        val title: String,
        val averageVote: String,
        override val type: ContentType
    ) : SearchContentUiItem(id, type = type)

    companion object {
        const val DEFAULT_ID = -1
    }
}
