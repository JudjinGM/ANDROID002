package com.judjingm.android002.profile.data.impl

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.BaseExceptionToErrorEntityMapper
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.profile.data.api.ProfileRemoteDataSource
import com.judjingm.android002.profile.di.ProfileExceptionMapper
import com.judjingm.android002.profile.domain.ProfileDomainMapper
import com.judjingm.android002.profile.domain.models.CreateSessionRequestDomain
import com.judjingm.android002.profile.domain.models.DeleteSession
import com.judjingm.android002.profile.domain.models.DeleteSessionRequestDomain
import com.judjingm.android002.profile.domain.models.RequestToken
import com.judjingm.android002.profile.domain.models.Session
import com.judjingm.android002.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProfileRemoteDataSource,
    private val domainMapper: ProfileDomainMapper,
    @ProfileExceptionMapper private val exceptionToErrorMapper: BaseExceptionToErrorEntityMapper
) : ProfileRepository {
    override fun createRequestToken(): Flow<Resource<RequestToken, ErrorEntity>> =
        flow {
            try {
                emit(
                    Resource.Success(
                        data = domainMapper.toRequestToken(
                            remoteDataSource.createRequestToken()
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

    override fun createSession(createSessionRequestDomain: CreateSessionRequestDomain): Flow<Resource<Session, ErrorEntity>> =
        flow {
            try {
                emit(
                    Resource.Success(
                        data = domainMapper.toCreateSession(
                            remoteDataSource.createSession(
                                domainMapper.toCreateSessionRequestDto(
                                    createSessionRequestDomain
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

    override fun deleteSession(deleteSessionRequestDomain: DeleteSessionRequestDomain): Flow<Resource<DeleteSession, ErrorEntity>> =
        flow {
            try {
                emit(
                    Resource.Success(
                        data = domainMapper.toDeleteSession(
                            remoteDataSource.deleteSession(
                                domainMapper.toDeleteSessionRequestDto(
                                    deleteSessionRequestDomain
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