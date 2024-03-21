package com.judjingm.android002.content.domain.repository

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.content.domain.models.Credits
import com.judjingm.android002.content.domain.models.MovieDetails
import com.judjingm.android002.content.domain.models.TvShowDetails
import kotlinx.coroutines.flow.Flow

interface ContentDetailsRepository {
    fun getMovieDetail(movieId: Int): Flow<Resource<MovieDetails, ErrorEntity>>
    fun getTvShowDetail(seriesId: Int): Flow<Resource<TvShowDetails, ErrorEntity>>

    fun getMovieCredits(movieId: Int): Flow<Resource<Credits, ErrorEntity>>
    fun getTvShowCredits(seriesId: Int): Flow<Resource<Credits, ErrorEntity>>
}