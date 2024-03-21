package com.judjingm.android002.content.domain.useCase

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.content.domain.models.Credits
import com.judjingm.android002.content.domain.repository.ContentDetailsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetMovieCreditsUseCase {
    fun execute(movieId: Int): Flow<Resource<Credits, ErrorEntity>>

    class Base @Inject constructor(private val repository: ContentDetailsRepository) :
        GetMovieCreditsUseCase {
        override fun execute(movieId: Int): Flow<Resource<Credits, ErrorEntity>> {
            return repository.getMovieCredits(movieId)
        }
    }
}