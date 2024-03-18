package com.judjingm.android002.home.data

import com.judjingm.android002.home.data.models.dto.MovieDto
import com.judjingm.android002.home.data.models.dto.MoviesDto
import com.judjingm.android002.home.data.models.dto.PopularMoviesQueryDto
import com.judjingm.android002.home.data.models.dto.PopularTVSeriesQueryDto
import com.judjingm.android002.home.data.models.dto.TvShowDto
import com.judjingm.android002.home.data.models.dto.TvSeriesDto
import com.judjingm.android002.home.data.models.request.PopularMoviesRequest
import com.judjingm.android002.home.data.models.request.PopularTVShowsRequest
import com.judjingm.android002.home.data.models.response.MovieResults
import com.judjingm.android002.home.data.models.response.MoviesResponse
import com.judjingm.android002.home.data.models.response.TVShowsResponse
import com.judjingm.android002.home.data.models.response.TVShowsResults

class ContentDataMapper {

    fun toPopularMovieRequest(popularMoviesQueryDto: PopularMoviesQueryDto): PopularMoviesRequest {
        return PopularMoviesRequest(
            page = popularMoviesQueryDto.page,
            language = popularMoviesQueryDto.language,
            region = popularMoviesQueryDto.region
        )
    }

    fun toPopularTvSeriesRequest(popularTVSeriesQueryDto: PopularTVSeriesQueryDto): PopularTVShowsRequest {
        return PopularTVShowsRequest(
            page = popularTVSeriesQueryDto.page,
            language = popularTVSeriesQueryDto.language
        )
    }

    fun toMoviesDto(moviesResponse: MoviesResponse): MoviesDto {
        return MoviesDto(
            page = moviesResponse.page,
            results = moviesResponse.results.map { toMovieDto(it) },
            totalPages = moviesResponse.totalPages,
            totalResults = moviesResponse.totalResults
        )
    }

    fun toTvSeriesDto(tvShowsResponse: TVShowsResponse): TvSeriesDto {
        return TvSeriesDto(
            page = tvShowsResponse.page,
            results = tvShowsResponse.results.map { toTvShowDto(it) },
            totalPages = tvShowsResponse.totalPages,
            totalResults = tvShowsResponse.totalResults
        )
    }

    fun toMovieDto(movieResults: MovieResults): MovieDto {
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

    fun toTvShowDto(tvShowResults: TVShowsResults): TvShowDto {
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