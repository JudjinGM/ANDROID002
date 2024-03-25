package com.judjingm.android002.home.presentation.ui

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.domain.models.Content
import com.judjingm.android002.home.presentation.models.ContentType
import com.judjingm.android002.home.presentation.models.PopularContentUi
import com.judjingm.android002.search.presentation.models.SearchContentUiItem
import javax.inject.Inject

class ContentDomainToUiMapper @Inject constructor() {
    fun <Domain, Ui> toPagedList(
        pagedList: PagedList<Domain>,
        convertFunc: (Domain) -> Ui
    ): PagedList<Ui> = with(pagedList) {
        PagedList(
            totalElements = totalElements,
            totalPages = totalPages,
            currentPage = currentPage,
            content = content.map(convertFunc)
        )
    }

    fun toPopularContent(movie: Content.Movie): PopularContentUi {
        return PopularContentUi(
            id = movie.id ?: DEFAULT_ID,
            posterPath = movie.posterPath ?: EMPTY_STRING,
            releaseDate = movie.releaseDate ?: EMPTY_STRING,
            title = movie.title ?: EMPTY_STRING,
            type = ContentType.MOVIE
        )
    }

    fun toPopularContent(tvShow: Content.TVShow): PopularContentUi {
        return PopularContentUi(
            id = tvShow.id ?: DEFAULT_ID,
            posterPath = tvShow.posterPath ?: EMPTY_STRING,
            releaseDate = tvShow.firstAirDate ?: EMPTY_STRING,
            title = tvShow.name ?: EMPTY_STRING,
            type = ContentType.TVSHOW
        )
    }

    fun toSearchContentUiItem(content: Content): SearchContentUiItem {
        return when (content) {
            is Content.Movie -> toSearchMovieUiItem(content)
            is Content.TVShow -> toSearchTvShowUiItem(content)
        }
    }

    private fun toSearchMovieUiItem(movie: Content.Movie): SearchContentUiItem.MovieUiItem {
        val releaseDate = if (movie.releaseDate.isNullOrBlank()) EMPTY_STRING
        else movie.releaseDate.substring(0, 4)
        return SearchContentUiItem.MovieUiItem(
            id = movie.id ?: DEFAULT_ID,
            posterPath = movie.posterPath ?: EMPTY_STRING,
            releaseDate = releaseDate,
            title = movie.title ?: EMPTY_STRING,
            type = ContentType.MOVIE,
            averageVote = String.format("%.1f", movie.voteAverage)
        )
    }

    private fun toSearchTvShowUiItem(tvShow: Content.TVShow): SearchContentUiItem.TvShowUiItem {
        val firstAirDate = if (tvShow.firstAirDate.isNullOrBlank()) EMPTY_STRING
        else tvShow.firstAirDate.substring(0, 4)
        return SearchContentUiItem.TvShowUiItem(
            id = tvShow.id ?: DEFAULT_ID,
            posterPath = tvShow.posterPath ?: EMPTY_STRING,
            firstAirDate = firstAirDate,
            title = tvShow.name ?: EMPTY_STRING,
            type = ContentType.TVSHOW,
            averageVote = String.format("%.1f", tvShow.voteAverage)
        )
    }

    companion object {
        private const val DEFAULT_ID = -1
        private const val EMPTY_STRING = ""
    }

}