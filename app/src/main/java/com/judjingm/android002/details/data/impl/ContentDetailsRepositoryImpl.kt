package com.judjingm.android002.details.data.impl

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.BaseExceptionToErrorEntityMapper
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.details.data.api.ContentDetailsRemoteDataSource
import com.judjingm.android002.details.di.ContentDetailsExceptionMapper
import com.judjingm.android002.details.domain.ContentDetailsDomainMapper
import com.judjingm.android002.details.domain.models.Credits
import com.judjingm.android002.details.domain.models.CreditsQuery
import com.judjingm.android002.details.domain.models.MovieDetails
import com.judjingm.android002.details.domain.models.MovieDetailsQuery
import com.judjingm.android002.details.domain.models.TvShowDetails
import com.judjingm.android002.details.domain.models.TvShowDetailsQuery
import com.judjingm.android002.details.domain.repository.ContentDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ContentDetailsRepositoryImpl @Inject constructor(
    private val remoteDataSource: ContentDetailsRemoteDataSource,
    private val domainMapper: ContentDetailsDomainMapper,
    @ContentDetailsExceptionMapper private val exceptionToErrorMapper: BaseExceptionToErrorEntityMapper
) : ContentDetailsRepository {
    override fun getMovieDetail(movieDetailsQuery: MovieDetailsQuery): Flow<Resource<MovieDetails, ErrorEntity>> =
        flow {
            try {
                emit(
                    Resource.Success(
                        data = domainMapper.toMovieDetails(
                            remoteDataSource.getMovieDetail(
                                domainMapper.toMovieDetailsQueryDto(
                                    movieDetailsQuery
                                )
                            )
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

    override fun getTvShowDetail(tvShowDetailsQuery: TvShowDetailsQuery): Flow<Resource<TvShowDetails, ErrorEntity>> =
        flow {
            try {
                emit(
                    Resource.Success(
                        data = domainMapper.toTvShowDetails(
                            remoteDataSource.getTvShowDetail(
                                domainMapper.toTvShowDetailsQueryDto(
                                    tvShowDetailsQuery
                                )
                            )
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

    override fun getMovieCredits(creditsQuery: CreditsQuery): Flow<Resource<Credits, ErrorEntity>> =
        flow {
            try {
                emit(
                    Resource.Success(
                        data = domainMapper.toCredits(
                            remoteDataSource.getMovieCredits(
                                domainMapper.toCreditsQueryDto(
                                    creditsQuery
                                )
                            )
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

    override fun getTvShowCredits(creditsQuery: CreditsQuery): Flow<Resource<Credits, ErrorEntity>> =
        flow {
            try {
                emit(
                    Resource.Success(
                        data = domainMapper.toCredits(
                            remoteDataSource.getTvShowCredits(
                                domainMapper.toCreditsQueryDto(
                                    creditsQuery
                                )
                            )
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