package com.judjingm.android002.home.data.api

import com.judjingm.android002.home.data.models.dto.MoviesDto
import com.judjingm.android002.home.data.models.dto.PopularMoviesQueryDto
import com.judjingm.android002.home.data.models.dto.PopularTVShowsQueryDto
import com.judjingm.android002.home.data.models.dto.TvShowsDto

interface PopularContentRemoteDataSource {
    suspend fun getPopularMovies(popularMoviesQuery: PopularMoviesQueryDto): MoviesDto
    suspend fun getPopularSeriesShows(popularTVShowsQueryDto: PopularTVShowsQueryDto): TvShowsDto
}