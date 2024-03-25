package com.judjingm.android002.search.domain.useCase

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.domain.models.Content
import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.search.domain.models.SearchMoviesQuery
import com.judjingm.android002.search.domain.repository.SearchContentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetSearchMoviesUseCase {
    operator fun invoke(
        query: String,
        page: Int,
        language: String
    ): Flow<Resource<PagedList<Content.Movie>, ErrorEntity>>

    class Base @Inject constructor(
        private val repository: SearchContentRepository
    ) : GetSearchMoviesUseCase {
        override fun invoke(
            query: String,
            page: Int,
            language: String
        ): Flow<Resource<PagedList<Content.Movie>, ErrorEntity>> {
            return repository.getSearchMovies(
                SearchMoviesQuery(
                    query = query,
                    language = language,
                    page = page,
                    region = DEFAULT_REGION
                )
            )
        }
    }

    companion object {
        private const val DEFAULT_REGION = ""
    }
}