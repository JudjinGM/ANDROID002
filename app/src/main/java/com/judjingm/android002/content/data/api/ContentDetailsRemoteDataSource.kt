package com.judjingm.android002.content.data.api

import com.judjingm.android002.content.data.models.dto.CreditsDto
import com.judjingm.android002.content.data.models.dto.CreditsQueryDto
import com.judjingm.android002.content.data.models.dto.MovieDetailsDto
import com.judjingm.android002.content.data.models.dto.MovieDetailsQueryDto
import com.judjingm.android002.content.data.models.dto.TvShowDetailsDto
import com.judjingm.android002.content.data.models.dto.TvShowDetailsQueryDto

interface ContentDetailsRemoteDataSource {
    suspend fun getMovieDetail(movieDetailsQueryDto: MovieDetailsQueryDto): MovieDetailsDto
    suspend fun getTvShowDetail(tvShowDetailsQueryDto: TvShowDetailsQueryDto): TvShowDetailsDto

    suspend fun getMovieCredits(creditsQueryDto: CreditsQueryDto): CreditsDto
    suspend fun getTvShowCredits(creditsQueryDto: CreditsQueryDto): CreditsDto
}