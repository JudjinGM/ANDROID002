package com.judjingm.android002.home.domain.useCase

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.home.domain.models.PopularTVShowsQuery
import com.judjingm.android002.home.domain.models.TVShow
import com.judjingm.android002.home.domain.repository.PopularContentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetPopularTVShowsUseCase {
    operator fun invoke(page: Int, language: String): Flow<Resource<PagedList<TVShow>, ErrorEntity>>
    class Base @Inject constructor(
        private val repository: PopularContentRepository
    ) : GetPopularTVShowsUseCase {
        override fun invoke(
            page: Int,
            language: String
        ): Flow<Resource<PagedList<TVShow>, ErrorEntity>> {
            return repository.getPopularTvShows(
                PopularTVShowsQuery(
                    language = language,
                    page = page,
                )
            )
        }
    }

}