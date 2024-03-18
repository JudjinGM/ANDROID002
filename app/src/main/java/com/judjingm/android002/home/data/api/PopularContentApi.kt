package com.judjingm.android002.home.data.api

import com.judjingm.android002.home.data.models.request.PopularMoviesRequest
import com.judjingm.android002.home.data.models.request.PopularTVShowsRequest
import com.judjingm.android002.home.data.models.response.MoviesResponse
import com.judjingm.android002.home.data.models.response.TVShowsResponse
import retrofit2.http.GET

interface PopularContentApi {
    @GET("/movie/popular")
    suspend fun getPopularMovies(popularMoviesRequest: PopularMoviesRequest): MoviesResponse

    @GET("/tv/popular")
    suspend fun getPopularTvSeries(popularTVSeriesRequest: PopularTVShowsRequest): TVShowsResponse
}