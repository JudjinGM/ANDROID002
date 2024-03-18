package com.judjingm.android002.home.domain.useCase

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.home.domain.models.PopularTVShowsQuery
import com.judjingm.android002.home.domain.models.TVShow
import com.judjingm.android002.home.domain.repository.PopularContentRepository
import kotlinx.coroutines.flow.Flow

interface GetPopularTVShowsUseCase {
    fun execute(page: Int): Flow<Resource<PagedList<TVShow>, ErrorEntity>>
    class Base(private val repository: PopularContentRepository) : GetPopularTVShowsUseCase {
        override fun execute(page: Int): Flow<Resource<PagedList<TVShow>, ErrorEntity>> {
            return repository.getPopularTvShows(
                PopularTVShowsQuery(
                    language = DEFAULT_LANGUAGE,
                    page = page,
                )
            )
        }
    }

    companion object {
        private const val DEFAULT_LANGUAGE = ""
    }

}