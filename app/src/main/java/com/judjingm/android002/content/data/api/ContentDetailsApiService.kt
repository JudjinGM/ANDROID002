package com.judjingm.android002.content.data.api

import com.judjingm.android002.content.data.models.response.CreditsResponse
import com.judjingm.android002.content.data.models.response.MovieDetailResponse
import com.judjingm.android002.content.data.models.response.TvShowDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ContentDetailsApiService {
    @GET("/3//movie/{movie_id}")
    suspend fun getMovieDetail(@Path("movie_id") movieId: Int): MovieDetailResponse

    @GET("/3//movie/{movie_id}/credits")
    suspend fun getMovieCredits(@Path("movie_id") movieId: Int): CreditsResponse

    @GET("/3//tv/{series_id}")
    suspend fun getTvShowDetail(@Path("series_id") seriesId: Int): TvShowDetailResponse

    @GET("/3//tv/{series_id}/credits")
    suspend fun getTvShowCredits(@Path("series_id") seriesId: Int): CreditsResponse
}