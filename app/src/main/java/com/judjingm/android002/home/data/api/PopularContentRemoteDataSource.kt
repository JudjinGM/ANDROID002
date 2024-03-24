package com.judjingm.android002.home.data.api

import com.judjingm.android002.common.data.models.MovieDto
import com.judjingm.android002.common.data.models.TvShowDto
import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.home.data.models.dto.PopularMoviesQueryDto
import com.judjingm.android002.home.data.models.dto.PopularTVShowsQueryDto

interface PopularContentRemoteDataSource {
    suspend fun getPopularMovies(popularMoviesQueryDto: PopularMoviesQueryDto): PagedList<MovieDto>
    suspend fun getPopularTVShows(popularTVShowsQueryDto: PopularTVShowsQueryDto): PagedList<TvShowDto>
}