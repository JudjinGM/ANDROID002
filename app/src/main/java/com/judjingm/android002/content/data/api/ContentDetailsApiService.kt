package com.judjingm.android002.content.data.api

import com.judjingm.android002.content.data.models.response.CreditsResponse
import com.judjingm.android002.content.data.models.response.MovieDetailResponse
import com.judjingm.android002.content.data.models.response.TvShowDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ContentDetailsApiService {
    @GET("/movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") movieId: Int): MovieDetailResponse

    @GET("/movie/{movie_id}/credits")
    fun getMovieCredits(@Path(" movieId") movieId: Int): CreditsResponse

    @GET("/tv/{series_id}")
    fun getTvShowDetail(@Path("series_id") seriesId: Int): TvShowDetailResponse

    @GET("/tv/{series_id}/credits")
    fun getTvShowCredits(@Path("series_id") seriesId: Int): CreditsResponse
}