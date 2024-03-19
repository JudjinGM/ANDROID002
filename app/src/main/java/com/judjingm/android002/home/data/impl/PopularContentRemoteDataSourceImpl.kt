package com.judjingm.android002.home.data.impl

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.utill.exceptions.NetworkException
import com.judjingm.android002.home.data.ContentDataMapper
import com.judjingm.android002.home.data.NetworkToPopularContentExceptionMapper
import com.judjingm.android002.home.data.api.PopularContentApiService
import com.judjingm.android002.home.data.api.PopularContentRemoteDataSource
import com.judjingm.android002.home.data.models.dto.MovieDto
import com.judjingm.android002.home.data.models.dto.PopularMoviesQueryDto
import com.judjingm.android002.home.data.models.dto.PopularTVShowsQueryDto
import com.judjingm.android002.home.data.models.dto.TvShowDto
import javax.inject.Inject

class PopularContentRemoteDataSourceImpl @Inject constructor(
    private val popularContentApiService: PopularContentApiService,
    private val contentDataMapper: ContentDataMapper,
    private val networkToPopularContentExceptionMapper: NetworkToPopularContentExceptionMapper,

    ) : PopularContentRemoteDataSource {
    override suspend fun getPopularMovies(popularMoviesQueryDto: PopularMoviesQueryDto): PagedList<MovieDto> {
        val options = HashMap<String, String>()
        options[PAGE] = popularMoviesQueryDto.page.toString()
        return try {
            val response = popularContentApiService.getPopularMovies(
                options = options
            )
            contentDataMapper.toMoviesDto(response)
        } catch (exception: NetworkException) {
            throw networkToPopularContentExceptionMapper.handleException(exception = exception)
        }
    }

    override suspend fun getPopularTVShows(popularTVShowsQueryDto: PopularTVShowsQueryDto): PagedList<TvShowDto> {
        return try {
            val options = HashMap<String, String>()
            options[PAGE] = popularTVShowsQueryDto.page.toString()
            val response = popularContentApiService.getPopularTvSeries(
                options = options
            )
            contentDataMapper.toTvShowDto(response)
        } catch (exception: NetworkException) {
            throw networkToPopularContentExceptionMapper.handleException(exception = exception)
        }
    }

    companion object {
        const val LANGUAGE = "language"
        const val PAGE = "page"
        const val REGION = "region"
    }

}