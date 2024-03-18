package com.judjingm.android002.home.data.api

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.home.data.models.dto.MovieDto
import com.judjingm.android002.home.data.models.dto.PopularMoviesQueryDto
import com.judjingm.android002.home.data.models.dto.PopularTVShowsQueryDto
import com.judjingm.android002.home.data.models.dto.TvShowDto

interface PopularContentRemoteDataSource {
    suspend fun getPopularMovies(popularMoviesQueryDto: PopularMoviesQueryDto): PagedList<MovieDto>
    suspend fun getPopularTVShows(popularTVShowsQueryDto: PopularTVShowsQueryDto): PagedList<TvShowDto>
}