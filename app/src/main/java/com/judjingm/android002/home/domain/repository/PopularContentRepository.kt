package com.judjingm.android002.home.domain.repository

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.home.domain.models.Movie
import com.judjingm.android002.home.domain.models.TVShow
import kotlinx.coroutines.flow.Flow

interface PopularContentRepository {
    fun getPopularMovies(): Flow<Resource<PagedList<Movie>, ErrorEntity>>
    fun getPopularTvShows(): Flow<Resource<PagedList<TVShow>, ErrorEntity>>
}