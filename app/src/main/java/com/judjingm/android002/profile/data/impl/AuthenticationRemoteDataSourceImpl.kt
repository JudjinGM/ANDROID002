package com.judjingm.android002.profile.data.impl

import com.judjingm.android002.common.utill.exceptions.NetworkException
import com.judjingm.android002.profile.data.NetworkToAuthenticationExceptionMapper
import com.judjingm.android002.profile.data.ProfileDataMapper
import com.judjingm.android002.profile.data.api.AuthenticationApiService
import com.judjingm.android002.profile.data.api.AuthenticationRemoteDataSource
import com.judjingm.android002.profile.data.models.CreateSessionRequestDto
import com.judjingm.android002.profile.data.models.DeleteSessionDto
import com.judjingm.android002.profile.data.models.DeleteSessionRequestDto
import com.judjingm.android002.profile.data.models.ProfileDetailsDto
import com.judjingm.android002.profile.data.models.RequestTokenDto
import com.judjingm.android002.profile.data.models.SessionDto
import javax.inject.Inject

class AuthenticationRemoteDataSourceImpl @Inject constructor(
    private val profileApi: AuthenticationApiService,
    private val dataMapper: ProfileDataMapper,
    private val networkToAuthenticationExceptionMapper: NetworkToAuthenticationExceptionMapper
) : AuthenticationRemoteDataSource {
    override suspend fun createRequestToken(): RequestTokenDto {
        return try {
            val response = profileApi.createRequestToken()
            dataMapper.toRequestTokenDto(response)
        } catch (exception: NetworkException) {
            throw networkToAuthenticationExceptionMapper.handleException(exception)
        }
    }

    override suspend fun createSession(createSessionRequestDto: CreateSessionRequestDto): SessionDto {
        return try {
            val response =
                profileApi.createSession(dataMapper.toCreateSessionRequest(createSessionRequestDto))
            dataMapper.toCreateSessionDto(response)
        } catch (exception: NetworkException) {
            throw networkToAuthenticationExceptionMapper.handleException(exception)
        }
    }

    override suspend fun deleteSession(deleteSessionRequestDto: DeleteSessionRequestDto): DeleteSessionDto {
        return try {
            val response =
                profileApi.deleteSession(dataMapper.toDeleteSessionRequest(deleteSessionRequestDto).sessionId)
            dataMapper.toDeleteSessionDto(response)
        } catch (exception: NetworkException) {
            throw networkToAuthenticationExceptionMapper.handleException(exception)
        }
    }

    override suspend fun getProfileDetails(sessionId: String): ProfileDetailsDto {
        return try {
            val response =
                profileApi.getProfileDetails(sessionId = sessionId)
            dataMapper.toProfileDetailsDto(response)
        } catch (exception: NetworkException) {
            throw networkToAuthenticationExceptionMapper.handleException(exception)
        }
    }
}