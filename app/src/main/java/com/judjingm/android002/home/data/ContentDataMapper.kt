package com.judjingm.android002.home.data

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.home.data.models.dto.MovieDto
import com.judjingm.android002.home.data.models.dto.PopularMoviesQueryDto
import com.judjingm.android002.home.data.models.dto.PopularTVShowsQueryDto
import com.judjingm.android002.home.data.models.dto.TvShowDto
import com.judjingm.android002.home.data.models.request.PopularMoviesRequest
import com.judjingm.android002.home.data.models.request.PopularTVShowsRequest
import com.judjingm.android002.home.data.models.response.MovieResults
import com.judjingm.android002.home.data.models.response.MoviesResponse
import com.judjingm.android002.home.data.models.response.TVShowsResponse
import com.judjingm.android002.home.data.models.response.TVShowResults

class ContentDataMapper {

    fun toPopularMovieRequest(popularMoviesQueryDto: PopularMoviesQueryDto): PopularMoviesRequest {
        return PopularMoviesRequest(
            page = popularMoviesQueryDto.page,
            language = popularMoviesQueryDto.language,
            region = popularMoviesQueryDto.region
        )
    }

    fun toPopularTvSeriesRequest(popularTVShowsQueryDto: PopularTVShowsQueryDto): PopularTVShowsRequest {
        return PopularTVShowsRequest(
            page = popularTVShowsQueryDto.page,
            language = popularTVShowsQueryDto.language
        )
    }

    fun toMoviesDto(moviesResponse: MoviesResponse): PagedList<MovieDto> {
        return PagedList(
            currentPage = moviesResponse.page,
            content = moviesResponse.results.map { toMovieDto(it) },
            totalPages = moviesResponse.totalPages,
            totalElements = moviesResponse.totalResults
        )
    }

    fun toTvShowDto(tvShowsResponse: TVShowsResponse): PagedList<TvShowDto> {
        return PagedList(
            currentPage = tvShowsResponse.page,
            content = tvShowsResponse.results.map { toTvShowDto(it) },
            totalPages = tvShowsResponse.totalPages,
            totalElements = tvShowsResponse.totalResults
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