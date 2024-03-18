package com.judjingm.android002.home.data.impl

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.BaseExceptionToErrorMapper
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.home.data.api.PopularContentRemoteDataSource
import com.judjingm.android002.home.domain.PopularContentDomainMapper
import com.judjingm.android002.home.domain.models.Movie
import com.judjingm.android002.home.domain.models.PopularMoviesQuery
import com.judjingm.android002.home.domain.models.PopularTVShowsQuery
import com.judjingm.android002.home.domain.models.TVShow
import com.judjingm.android002.home.domain.repository.PopularContentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PopularContentRepositoryImpl(
    private val remoteDataSource: PopularContentRemoteDataSource,
    private val domainMapper: PopularContentDomainMapper,
    private val exceptionToErrorMapper: BaseExceptionToErrorMapper
) : PopularContentRepository {
    override fun getPopularMovies(
        popularMoviesQuery: PopularMoviesQuery
    ): Flow<Resource<PagedList<Movie>, ErrorEntity>> = flow {
        try {
            val data = remoteDataSource.getPopularMovies(
                popularMoviesQueryDto = domainMapper.toPopularMoviesQueryDto(popularMoviesQuery)
            )

            Resource.Success<PagedList<Movie>, ErrorEntity>(
                data = domainMapper.toPagedList(data) {
                    domainMapper.toMovie(it)
                })

        } catch (exception: Exception) {
            Resource.Error<PagedList<Movie>, ErrorEntity>(
                exceptionToErrorMapper.handleException(exception)
            )
        }
    }

    override fun getPopularTvShows(
        popularTVShowsQuery: PopularTVShowsQuery
    ): Flow<Resource<PagedList<TVShow>, ErrorEntity>> = flow {
        try {
            val data = remoteDataSource.getPopularTVShows(
                popularTVShowsQueryDto = domainMapper.toPopularTVShowsQueryDto(
                    popularTVShowsQuery
                )
            )

            Resource.Success<PagedList<TVShow>, ErrorEntity>(
                data = domainMapper.toPagedList(data) {
                    domainMapper.toTVShow(it)
                })

        } catch (exception: Exception) {
            Resource.Error<PagedList<Movie>, ErrorEntity>(
                exceptionToErrorMapper.handleException(exception)
            )
        }
    }

}