package com.judjingm.android002.home.data

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.home.data.models.dto.MovieDto
import com.judjingm.android002.home.data.models.dto.TvShowDto
import com.judjingm.android002.home.data.models.response.MovieResults
import com.judjingm.android002.home.data.models.response.MoviesResponse
import com.judjingm.android002.home.data.models.response.TVShowResults
import com.judjingm.android002.home.data.models.response.TVShowsResponse
import javax.inject.Inject

class ContentDataMapper @Inject constructor() {

    fun toMoviesDto(moviesResponse: MoviesResponse): PagedList<MovieDto> {
        return PagedList(
            currentPage = moviesResponse.page ?: 0,
            content = moviesResponse.results.map { toMovieDto(it) },
            totalPages = moviesResponse.totalPages ?: 0,
            totalElements = moviesResponse.totalResults ?: 0
        )
    }

    fun toTvShowDto(tvShowsResponse: TVShowsResponse): PagedList<TvShowDto> {
        return PagedList(
            currentPage = tvShowsResponse.page ?: 0,
            content = tvShowsResponse.results.map { toTvShowDto(it) },
            totalPages = tvShowsResponse.totalPages ?: 0,
            totalElements = tvShowsResponse.totalResults ?: 0
        )
    }

    private fun toMovieDto(movieResults: MovieResults): MovieDto {
        with(movieResults) {
            return MovieDto(
                adult = adult,
                backdropPath = backdropPath,
                genreIds = genreIds,
                id = id,
                originalLanguage = originalLanguage,
                originalTitle = originalTitle,
                overview = overview,
                popularity = popularity,
                posterPath = posterPath,
                releaseDate = releaseDate,
                title = title,
                video = video,
                voteAverage = voteAverage,
                voteCount = voteCount
            )
        }
    }

    private fun toTvShowDto(tvShowResults: TVShowResults): TvShowDto {
        with(tvShowResults) {
            return TvShowDto(
                adult = adult,
                backdropPath = backdropPath,
                genreIds = genreIds,
                id = id,
                originCountry = originCountry,
                originalLanguage = originalLanguage,
                originalName = originalName,
                overview = overview,
                popularity = popularity,
                posterPath = posterPath,
                firstAirDate = firstAirDate,
                name = name,
                voteAverage = voteAverage,
                voteCount = voteCount
            )
        }
    }

}