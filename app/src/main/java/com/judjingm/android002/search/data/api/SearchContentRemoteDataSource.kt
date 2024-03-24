package com.judjingm.android002.search.data.api

import com.judjingm.android002.common.data.models.MovieDto
import com.judjingm.android002.common.data.models.TvShowDto
import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.search.data.models.dto.SearchMoviesQueryDto
import com.judjingm.android002.search.data.models.dto.SearchTvShowsQueryDto

interface SearchContentRemoteDataSource {
    suspend fun getSearchMovies(searchMoviesQueryDto: SearchMoviesQueryDto): PagedList<MovieDto>
    suspend fun getSearchTvShows(searchTVShowsQueryDto: SearchTvShowsQueryDto): PagedList<TvShowDto>
}