package com.judjingm.android002.home.presentation.ui

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.domain.models.Movie
import com.judjingm.android002.common.domain.models.TVShow
import com.judjingm.android002.home.presentation.models.ContentType
import com.judjingm.android002.home.presentation.models.MovieUi.Companion.DEFAULT_ID
import com.judjingm.android002.home.presentation.models.MovieUi.Companion.EMPTY_STRING
import com.judjingm.android002.home.presentation.models.PopularContentUi
import javax.inject.Inject

class PopularContentDomainToUiMapper @Inject constructor() {
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

    fun toPopularContent(movie: Movie): PopularContentUi {
        return PopularContentUi(
            id = movie.id ?: DEFAULT_ID,
            posterPath = movie.posterPath ?: EMPTY_STRING,
            releaseDate = movie.releaseDate ?: EMPTY_STRING,
            title = movie.title ?: EMPTY_STRING,
            type = ContentType.MOVIE
        )
    }

    fun toPopularContent(tvShow: TVShow): PopularContentUi {
        return PopularContentUi(
            id = tvShow.id ?: DEFAULT_ID,
            posterPath = tvShow.posterPath ?: EMPTY_STRING,
            releaseDate = tvShow.firstAirDate ?: EMPTY_STRING,
            title = tvShow.name ?: EMPTY_STRING,
            type = ContentType.TVSHOW
        )
    }

}