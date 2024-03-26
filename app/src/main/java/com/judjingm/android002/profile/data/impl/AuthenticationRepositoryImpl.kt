package com.judjingm.android002.profile.data.impl

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.BaseExceptionToErrorEntityMapper
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.profile.data.api.AuthenticationRemoteDataSource
import com.judjingm.android002.profile.di.AuthenticationExceptionMapper
import com.judjingm.android002.profile.domain.AuthenticationDomainMapper
import com.judjingm.android002.profile.domain.models.AuthenticationState
import com.judjingm.android002.profile.domain.models.DeleteSession
import com.judjingm.android002.profile.domain.models.ProfileDetails
import com.judjingm.android002.profile.domain.models.RequestToken
import com.judjingm.android002.profile.domain.models.Session
import com.judjingm.android002.profile.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthenticationRemoteDataSource,
    private val domainMapper: AuthenticationDomainMapper,
    @AuthenticationExceptionMapper private val exceptionToErrorMapper: BaseExceptionToErrorEntityMapper
) : AuthenticationRepository {
    private val _state: MutableStateFlow<AuthenticationState> =
        MutableStateFlow(AuthenticationState())
    private val state = _state.asStateFlow()
    private val currentState: AuthenticationState
        get() = state.replayCache.firstOrNull()
            ?: AuthenticationState(AuthenticationState.State.Initial)

    override fun isAuthenticationInProgress(): Flow<Boolean> {
        return state.map { it.state is AuthenticationState.State.InProcess }
    }

    override suspend fun createRequestToken(
    ): Resource<RequestToken, ErrorEntity> {
        return try {
            val data = domainMapper.toRequestToken(
                remoteDataSource.createRequestToken()
            )
            _state.update {
                it.copy(state = AuthenticationState.State.InProcess(requestToken = data.requestToken))
            }

            Resource.Success(data = data)

        } catch (exception: Exception) {
            return Resource.Error(
                exceptionToErrorMapper.handleException(exception)
            )
        }
    }

    override suspend fun createSession(): Resource<Session, ErrorEntity> {
        return try {
            val token: String

            when (val state = currentState.state) {
                is AuthenticationState.State.InProcess -> {
                    token = state.requestToken
                }

                AuthenticationState.State.Initial -> {
                    return Resource.Error(
                        ErrorEntity.Authentication.InvalidToken(
                            WRONG_STATE_ERROR
                        )
                    )
                }
            }

            val data = domainMapper.toCreateSession(
                remoteDataSource.createSession(
                    domainMapper.toCreateSessionRequestDto(token)
                )
            )
            _state.update {
                it.copy(state = AuthenticationState.State.Initial)
            }

            Resource.Success(
                data = data
            )

        } catch (exception: Exception) {
            return Resource.Error(
                exceptionToErrorMapper.handleException(exception)
            )
        }
    }

    override suspend fun deleteSession(sessionId: String): Resource<DeleteSession, ErrorEntity> {
        return try {
            Resource.Success(
                data = domainMapper.toDeleteSession(
                    remoteDataSource.deleteSession(
                        domainMapper.toDeleteSessionRequestDto(
                            sessionId
                        )
                    )
                )
            )

        } catch (exception: Exception) {
            return Resource.Error(
                exceptionToErrorMapper.handleException(exception)
            )
        }
    }

    override suspend fun getProfileDetails(sessionId: String): Resource<ProfileDetails, ErrorEntity> {
        return try {
            Resource.Success(
                data = domainMapper.toProfileDetails(
                    remoteDataSource.getProfileDetails(sessionId)
                )
            )

        } catch (exception: Exception) {
            return Resource.Error(
                exceptionToErrorMapper.handleException(exception)
            )
        }
    }

    companion object {
        const val WRONG_STATE_ERROR = "Authentication state is not in progress"
    }

}