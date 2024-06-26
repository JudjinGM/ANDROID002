package com.judjingm.android002.details.domain.repository

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.details.domain.models.Credits
import com.judjingm.android002.details.domain.models.CreditsQuery
import com.judjingm.android002.details.domain.models.MovieDetails
import com.judjingm.android002.details.domain.models.MovieDetailsQuery
import com.judjingm.android002.details.domain.models.TvShowDetails
import com.judjingm.android002.details.domain.models.TvShowDetailsQuery
import kotlinx.coroutines.flow.Flow

interface ContentDetailsRepository {
    fun getMovieDetail(movieDetailsQuery: MovieDetailsQuery): Flow<Resource<MovieDetails, ErrorEntity>>
    fun getTvShowDetail(tvShowDetailsQuery: TvShowDetailsQuery): Flow<Resource<TvShowDetails, ErrorEntity>>

    fun getMovieCredits(creditsQuery: CreditsQuery): Flow<Resource<Credits, ErrorEntity>>
    fun getTvShowCredits(creditsQuery: CreditsQuery): Flow<Resource<Credits, ErrorEntity>>
}