package com.judjingm.android002.search.data.impl

import com.judjingm.android002.common.data.CommonContentDataMapper
import com.judjingm.android002.common.data.models.MovieDto
import com.judjingm.android002.common.data.models.TvShowDto
import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.utill.exceptions.NetworkException
import com.judjingm.android002.search.data.NetworkToSearchContentExceptionMapper
import com.judjingm.android002.search.data.api.SearchContentApiService
import com.judjingm.android002.search.data.api.SearchContentRemoteDataSource
import com.judjingm.android002.search.data.models.dto.SearchMoviesQueryDto
import com.judjingm.android002.search.data.models.dto.SearchTvShowsQueryDto
import javax.inject.Inject

class SearchContentRemoteDataSourceImpl @Inject constructor(
    private val searchContentApiService: SearchContentApiService,
    private val commonContentDataMapper: CommonContentDataMapper,
    private val networkToSearchContentExceptionMapper: NetworkToSearchContentExceptionMapper,
) : SearchContentRemoteDataSource {
    override suspend fun getSearchMovies(searchMoviesQueryDto: SearchMoviesQueryDto): PagedList<MovieDto> {
        val options = HashMap<String, String>()
        options[QUERY] = searchMoviesQueryDto.query
        options[PAGE] = searchMoviesQueryDto.page.toString()
        options[LANGUAGE] = searchMoviesQueryDto.language
        return try {
            val response = searchContentApiService.searchMovies(
                options = options
            )
            commonContentDataMapper.toMoviesDto(response)
        } catch (exception: NetworkException) {
            throw networkToSearchContentExceptionMapper.handleException(exception = exception)
        }
    }

    override suspend fun getSearchTvShows(searchTVShowsQueryDto: SearchTvShowsQueryDto): PagedList<TvShowDto> {
        return try {
            val options = HashMap<String, String>()
            options[QUERY] = searchTVShowsQueryDto.query
            options[PAGE] = searchTVShowsQueryDto.page.toString()
            options[LANGUAGE] = searchTVShowsQueryDto.language
            val response = searchContentApiService.searchTvSeries(
                options = options
            )
            commonContentDataMapper.toTvShowDto(response)
        } catch (exception: NetworkException) {
            throw networkToSearchContentExceptionMapper.handleException(exception = exception)
        }
    }

    companion object {
        const val QUERY = "query"
        const val LANGUAGE = "language"
        const val PAGE = "page"
    }

}