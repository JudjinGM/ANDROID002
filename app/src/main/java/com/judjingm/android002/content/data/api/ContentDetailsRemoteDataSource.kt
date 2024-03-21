package com.judjingm.android002.content.data.api

import com.judjingm.android002.content.data.models.dto.CreditsDto
import com.judjingm.android002.content.data.models.dto.MovieDetailsDto
import com.judjingm.android002.content.data.models.dto.TvShowDetailDto

interface ContentDetailsRemoteDataSource {
    suspend fun getMovieDetail(movieId: Int): MovieDetailsDto
    suspend fun getTvShowDetail(seriesId: Int): TvShowDetailDto

    suspend fun getMovieCredits(movieId: Int): CreditsDto
    suspend fun getTvShowCredits(seriesId: Int): CreditsDto
}