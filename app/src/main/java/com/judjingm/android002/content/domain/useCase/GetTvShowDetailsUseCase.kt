package com.judjingm.android002.content.domain.useCase

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.content.domain.models.TvShowDetails
import com.judjingm.android002.content.domain.models.TvShowDetailsQuery
import com.judjingm.android002.content.domain.repository.ContentDetailsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetTvShowDetailsUseCase {
    operator fun invoke(seriesId: Int, language: String): Flow<Resource<TvShowDetails, ErrorEntity>>

    class Base @Inject constructor(private val repository: ContentDetailsRepository) :
        GetTvShowDetailsUseCase {
        override fun invoke(
            seriesId: Int,
            language: String
        ): Flow<Resource<TvShowDetails, ErrorEntity>> {
            return repository.getTvShowDetail(
                TvShowDetailsQuery(
                    seriesId = seriesId,
                    language = language
                )
            )
        }
    }

}