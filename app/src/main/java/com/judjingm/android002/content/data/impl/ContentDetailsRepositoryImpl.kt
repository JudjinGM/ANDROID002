package com.judjingm.android002.content.data.impl

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.BaseExceptionToErrorEntityMapper
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.content.data.api.ContentDetailsRemoteDataSource
import com.judjingm.android002.content.di.ContentDetailsExceptionMapper
import com.judjingm.android002.content.domain.ContentDetailsDomainMapper
import com.judjingm.android002.content.domain.models.Credits
import com.judjingm.android002.content.domain.models.MovieDetails
import com.judjingm.android002.content.domain.models.TvShowDetails
import com.judjingm.android002.content.domain.repository.ContentDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ContentDetailsRepositoryImpl @Inject constructor(
    private val remoteDataSource: ContentDetailsRemoteDataSource,
    private val domainMapper: ContentDetailsDomainMapper,
    @ContentDetailsExceptionMapper private val exceptionToErrorMapper: BaseExceptionToErrorEntityMapper
) : ContentDetailsRepository {
    override fun getMovieDetail(movieId: Int): Flow<Resource<MovieDetails, ErrorEntity>> = flow {
        try {
            emit(
                Resource.Success(
                    data = domainMapper.toMovieDetails(
                        remoteDataSource.getMovieDetail(movieId)
                    )
                )
            )

        } catch (exception: Exception) {
            emit(
                Resource.Error(
                    exceptionToErrorMapper.handleException(exception)
                )
            )
        }
    }

    override fun getTvShowDetail(seriesId: Int): Flow<Resource<TvShowDetails, ErrorEntity>> = flow {
        try {
            emit(
                Resource.Success(
                    data = domainMapper.toTvShowDetails(
                        remoteDataSource.getTvShowDetail(seriesId)
                    )
                )
            )

        } catch (exception: Exception) {
            emit(
                Resource.Error(
                    exceptionToErrorMapper.handleException(exception)
                )
            )
        }
    }

    override fun getMovieCredits(movieId: Int): Flow<Resource<Credits, ErrorEntity>> = flow {
        try {
            emit(
                Resource.Success(
                    data = domainMapper.toCredits(
                        remoteDataSource.getMovieCredits(movieId)
                    )
                )
            )

        } catch (exception: Exception) {
            emit(
                Resource.Error(
                    exceptionToErrorMapper.handleException(exception)
                )
            )
        }
    }

    override fun getTvShowCredits(seriesId: Int): Flow<Resource<Credits, ErrorEntity>> = flow {
        try {
            emit(
                Resource.Success(
                    data = domainMapper.toCredits(
                        remoteDataSource.getTvShowCredits(seriesId)
                    )
                )
            )

        } catch (exception: Exception) {
            emit(
                Resource.Error(
                    exceptionToErrorMapper.handleException(exception)
                )
            )
        }
    }
}