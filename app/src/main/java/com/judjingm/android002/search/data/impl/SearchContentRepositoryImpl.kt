package com.judjingm.android002.search.data.impl

import com.judjingm.android002.common.domain.CommonContentDomainMapper
import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.domain.models.Content
import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.BaseExceptionToErrorEntityMapper
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.search.data.api.SearchContentRemoteDataSource
import com.judjingm.android002.search.di.SearchContentExceptionMapper
import com.judjingm.android002.search.domain.models.SearchMoviesQuery
import com.judjingm.android002.search.domain.models.SearchTvShowsQuery
import com.judjingm.android002.search.domain.repository.SearchContentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchContentRepositoryImpl @Inject constructor(
    private val remoteDataSource: SearchContentRemoteDataSource,
    private val domainMapper: CommonContentDomainMapper,
    @SearchContentExceptionMapper private val exceptionToErrorMapper: BaseExceptionToErrorEntityMapper
) : SearchContentRepository {
    override fun getSearchMovies(
        searchMoviesQuery: SearchMoviesQuery
    ): Flow<Resource<PagedList<Content.Movie>, ErrorEntity>> = flow {
        try {
            val data = remoteDataSource.getSearchMovies(
                searchMoviesQueryDto = domainMapper.toSearchMoviesQueryDto(searchMoviesQuery)
            )

            emit(
                Resource.Success(
                    data = domainMapper.toPagedList(data) {
                        domainMapper.toMovie(it)
                    })
            )

        } catch (exception: Exception) {
            emit(
                Resource.Error(
                    exceptionToErrorMapper.handleException(exception)
                )
            )
        }
    }

    override fun getSearchTvShows(
        searchTvShowsQuery: SearchTvShowsQuery
    ): Flow<Resource<PagedList<Content.TVShow>, ErrorEntity>> = flow {
        try {
            val data = remoteDataSource.getSearchTvShows(
                searchTVShowsQueryDto = domainMapper.toSearchTvShowsQueryDto(
                    searchTvShowsQuery
                )
            )

            emit(
                Resource.Success(data = domainMapper.toPagedList(data) {
                    domainMapper.toTVShow(it)
                })
            )

        } catch (exception: Exception) {
            emit(
                Resource.Error(
                    exceptionToErrorMapper.handleException(exception)
                )
            )
        }
    }

}