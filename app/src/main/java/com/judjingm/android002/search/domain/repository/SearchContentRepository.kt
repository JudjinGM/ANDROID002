package com.judjingm.android002.search.domain.repository

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.domain.models.Content
import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.search.domain.models.SearchMoviesQuery
import com.judjingm.android002.search.domain.models.SearchTvShowsQuery
import kotlinx.coroutines.flow.Flow

interface SearchContentRepository {
    fun getSearchMovies(searchMoviesQuery: SearchMoviesQuery): Flow<Resource<PagedList<Content.Movie>, ErrorEntity>>
    fun getSearchTvShows(searchTVShowsQuery: SearchTvShowsQuery): Flow<Resource<PagedList<Content.TVShow>, ErrorEntity>>

}