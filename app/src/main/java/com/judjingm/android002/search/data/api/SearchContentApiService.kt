package com.judjingm.android002.search.data.api

import com.judjingm.android002.common.data.models.response.MoviesResponse
import com.judjingm.android002.common.data.models.response.TVShowsResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface SearchContentApiService {
    @GET("3/search/movie")
    suspend fun searchMovies(@QueryMap options: Map<String, String>): MoviesResponse

    @GET("3/search/tv")
    suspend fun searchTvSeries(@QueryMap options: Map<String, String>): TVShowsResponse
}