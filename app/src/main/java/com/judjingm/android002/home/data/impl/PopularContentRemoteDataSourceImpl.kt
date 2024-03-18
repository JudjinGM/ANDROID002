package com.judjingm.android002.home.data.impl

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.utill.exceptions.NetworkException
import com.judjingm.android002.home.data.ContentDataMapper
import com.judjingm.android002.home.data.NetworkToPopularContentExceptionMapper
import com.judjingm.android002.home.data.api.PopularContentApi
import com.judjingm.android002.home.data.api.PopularContentRemoteDataSource
import com.judjingm.android002.home.data.models.dto.MovieDto
import com.judjingm.android002.home.data.models.dto.PopularMoviesQueryDto
import com.judjingm.android002.home.data.models.dto.PopularTVShowsQueryDto
import com.judjingm.android002.home.data.models.dto.TvShowDto

class PopularContentRemoteDataSourceImpl(
    private val popularContentApi: PopularContentApi,
    private val contentDataMapper: ContentDataMapper,
    private val networkToPopularContentExceptionMapper: NetworkToPopularContentExceptionMapper,

    ) : PopularContentRemoteDataSource {
    override suspend fun getPopularMovies(popularMoviesQueryDto: PopularMoviesQueryDto): PagedList<MovieDto> {
        return try {
            val response = popularContentApi.getPopularMovies(
                popularMoviesRequest = contentDataMapper.toPopularMovieRequest(popularMoviesQueryDto)
            )
            contentDataMapper.toMoviesDto(response)
        } catch (exception: NetworkException) {
            throw networkToPopularContentExceptionMapper.handleException(exception = exception)
        }
    }

    override suspend fun getPopularTVShows(popularTVShowsQueryDto: PopularTVShowsQueryDto): PagedList<TvShowDto> {
        return try {
            val response = popularContentApi.getPopularTvSeries(
                popularTVSeriesRequest = contentDataMapper.toPopularTvSeriesRequest(
                    popularTVShowsQueryDto = popularTVShowsQueryDto
                )
            )
            contentDataMapper.toTvShowDto(response)
        } catch (exception: NetworkException) {
            throw networkToPopularContentExceptionMapper.handleException(exception = exception)
        }
    }

}