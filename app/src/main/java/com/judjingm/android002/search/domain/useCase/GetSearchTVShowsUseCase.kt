package com.judjingm.android002.search.domain.useCase

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.domain.models.Content
import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.search.domain.models.SearchTvShowsQuery
import com.judjingm.android002.search.domain.repository.SearchContentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetSearchTVShowsUseCase {
    operator fun invoke(
        query: String,
        page: Int,
        language: String
    ): Flow<Resource<PagedList<Content.TVShow>, ErrorEntity>>

    class Base @Inject constructor(
        private val repository: SearchContentRepository
    ) : GetSearchTVShowsUseCase {
        override fun invoke(
            query: String,
            page: Int,
            language: String
        ): Flow<Resource<PagedList<Content.TVShow>, ErrorEntity>> {
            return repository.getSearchTvShows(
                SearchTvShowsQuery(
                    query = query,
                    language = language,
                    page = page,
                )
            )
        }
    }

}