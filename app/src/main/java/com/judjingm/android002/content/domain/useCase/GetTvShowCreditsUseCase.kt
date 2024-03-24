package com.judjingm.android002.content.domain.useCase

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.content.domain.models.Credits
import com.judjingm.android002.content.domain.models.CreditsQuery
import com.judjingm.android002.content.domain.repository.ContentDetailsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetTvShowCreditsUseCase {
    operator fun invoke(seriesId: Int, language: String): Flow<Resource<Credits, ErrorEntity>>

    class Base @Inject constructor(private val repository: ContentDetailsRepository) :
        GetTvShowCreditsUseCase {
        override fun invoke(seriesId: Int, language: String): Flow<Resource<Credits, ErrorEntity>> {
            return repository.getTvShowCredits(
                CreditsQuery(
                    contentId = seriesId,
                    language = language
                )
            )
        }
    }

}