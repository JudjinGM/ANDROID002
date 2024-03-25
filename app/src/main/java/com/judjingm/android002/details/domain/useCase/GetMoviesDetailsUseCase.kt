package com.judjingm.android002.details.domain.useCase

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.details.domain.models.MovieDetails
import com.judjingm.android002.details.domain.models.MovieDetailsQuery
import com.judjingm.android002.details.domain.repository.ContentDetailsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetMoviesDetailsUseCase {
    operator fun invoke(movieId: Int, language: String): Flow<Resource<MovieDetails, ErrorEntity>>

    class Base @Inject constructor(private val repository: ContentDetailsRepository) :
        GetMoviesDetailsUseCase {
        override fun invoke(
            movieId: Int,
            language: String
        ): Flow<Resource<MovieDetails, ErrorEntity>> {
            return repository.getMovieDetail(
                MovieDetailsQuery(
                    movieId = movieId,
                    language = language
                )
            )
        }
    }
}