package com.judjingm.android002.home.domain.repository

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.domain.models.Movie
import com.judjingm.android002.common.domain.models.TVShow
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.home.domain.models.PopularMoviesQuery
import com.judjingm.android002.home.domain.models.PopularTVShowsQuery
import kotlinx.coroutines.flow.Flow

interface PopularContentRepository {
    fun getPopularMovies(popularMoviesQuery: PopularMoviesQuery): Flow<Resource<PagedList<Movie>, ErrorEntity>>
    fun getPopularTvShows(popularTVShowsQuery: PopularTVShowsQuery): Flow<Resource<PagedList<TVShow>, ErrorEntity>>

}