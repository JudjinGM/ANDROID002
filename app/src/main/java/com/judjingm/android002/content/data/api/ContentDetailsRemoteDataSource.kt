package com.judjingm.android002.content.data.api

import com.judjingm.android002.content.data.models.dto.CreditsDto
import com.judjingm.android002.content.data.models.dto.MovieDetailsDto
import com.judjingm.android002.content.data.models.dto.TvShowDetailDto

interface ContentDetailsRemoteDataSource {
    fun getMovieDetail(movieId: Int): MovieDetailsDto
    fun getTvShowDetail(seriesId: Int): TvShowDetailDto

    fun getMovieCredits(movieId: Int): CreditsDto
    fun getTvShowCredits(seriesId: Int): CreditsDto
}