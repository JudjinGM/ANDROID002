package com.judjingm.android002.content.domain.useCase

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.content.domain.models.TvShowDetails
import com.judjingm.android002.content.domain.repository.ContentDetailsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetTvShowDetailsUseCase {
    fun execute(seriesId: Int): Flow<Resource<TvShowDetails, ErrorEntity>>

    class Base @Inject constructor(private val repository: ContentDetailsRepository) :
        GetTvShowDetailsUseCase {
        override fun execute(seriesId: Int): Flow<Resource<TvShowDetails, ErrorEntity>> {
            return repository.getTvShowDetail(seriesId)
        }
    }

}