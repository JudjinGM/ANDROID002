package com.judjingm.android002.home.data.impl

import com.judjingm.android002.common.data.CommonContentDataMapper
import com.judjingm.android002.common.data.models.MovieDto
import com.judjingm.android002.common.data.models.TvShowDto
import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.utill.exceptions.NetworkException
import com.judjingm.android002.home.data.NetworkToPopularContentExceptionMapper
import com.judjingm.android002.home.data.api.PopularContentApiService
import com.judjingm.android002.home.data.api.PopularContentRemoteDataSource
import com.judjingm.android002.home.data.models.dto.PopularMoviesQueryDto
import com.judjingm.android002.home.data.models.dto.PopularTVShowsQueryDto
import javax.inject.Inject

class PopularContentRemoteDataSourceImpl @Inject constructor(
    private val popularContentApiService: PopularContentApiService,
    private val commonContentDataMapper: CommonContentDataMapper,
    private val networkToPopularContentExceptionMapper: NetworkToPopularContentExceptionMapper,

    ) : PopularContentRemoteDataSource {
    override suspend fun getPopularMovies(popularMoviesQueryDto: PopularMoviesQueryDto): PagedList<MovieDto> {
        val options = HashMap<String, String>()
        options[PAGE] = popularMoviesQueryDto.page.toString()
        options[LANGUAGE] = popularMoviesQueryDto.language
        return try {
            val response = popularContentApiService.getPopularMovies(
                options = options
            )
            commonContentDataMapper.toMoviesDto(response)
        } catch (exception: NetworkException) {
            throw networkToPopularContentExceptionMapper.handleException(exception = exception)
        }
    }

    override suspend fun getPopularTVShows(popularTVShowsQueryDto: PopularTVShowsQueryDto): PagedList<TvShowDto> {
        return try {
            val options = HashMap<String, String>()
            options[PAGE] = popularTVShowsQueryDto.page.toString()
            options[LANGUAGE] = popularTVShowsQueryDto.language
            val response = popularContentApiService.getPopularTvSeries(
                options = options
            )
            commonContentDataMapper.toTvShowDto(response)
        } catch (exception: NetworkException) {
            throw networkToPopularContentExceptionMapper.handleException(exception = exception)
        }
    }

    companion object {
        const val LANGUAGE = "language"
        const val PAGE = "page"
    }

}