package com.judjingm.android002.home.domain.useCase

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.home.domain.models.Movie
import com.judjingm.android002.home.domain.models.PopularMoviesQuery
import com.judjingm.android002.home.domain.repository.PopularContentRepository
import kotlinx.coroutines.flow.Flow

interface GetPopularMoviesUseCase {
    fun execute(page: Int): Flow<Resource<PagedList<Movie>, ErrorEntity>>
    class Base(private val repository: PopularContentRepository) : GetPopularMoviesUseCase {
        override fun execute(page: Int): Flow<Resource<PagedList<Movie>, ErrorEntity>> {
            return repository.getPopularMovies(
                PopularMoviesQuery(
                    language = DEFAULT_LANGUAGE,
                    page = page,
                    region = DEFAULT_REGION
                )
            )
        }
    }

    companion object {
        private const val DEFAULT_LANGUAGE = ""
        private const val DEFAULT_REGION = ""
    }
}