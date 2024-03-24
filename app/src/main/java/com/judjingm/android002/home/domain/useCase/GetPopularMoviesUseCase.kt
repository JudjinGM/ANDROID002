package com.judjingm.android002.home.domain.useCase

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.home.domain.models.Movie
import com.judjingm.android002.home.domain.models.PopularMoviesQuery
import com.judjingm.android002.home.domain.repository.PopularContentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetPopularMoviesUseCase {
    operator fun invoke(page: Int, language: String): Flow<Resource<PagedList<Movie>, ErrorEntity>>
    class Base @Inject constructor(
        private val repository: PopularContentRepository
    ) : GetPopularMoviesUseCase {
        override fun invoke(
            page: Int,
            language: String
        ): Flow<Resource<PagedList<Movie>, ErrorEntity>> {
            return repository.getPopularMovies(
                PopularMoviesQuery(
                    language = language, page = page, region = DEFAULT_REGION
                )
            )
        }
    }

    companion object {
        private const val DEFAULT_REGION = ""
    }
}