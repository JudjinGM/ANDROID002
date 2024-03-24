package com.judjingm.android002.home.data.api

import com.judjingm.android002.common.data.models.response.MoviesResponse
import com.judjingm.android002.common.data.models.response.TVShowsResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface PopularContentApiService {
    @GET("/3/movie/popular")
    suspend fun getPopularMovies(@QueryMap options: Map<String, String>): MoviesResponse

    @GET("/3/tv/popular")
    suspend fun getPopularTvSeries(@QueryMap options: Map<String, String>): TVShowsResponse
}