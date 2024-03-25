package com.judjingm.android002.profile.data.impl

import com.judjingm.android002.common.utill.exceptions.NetworkException
import com.judjingm.android002.profile.data.NetworkToProfileExceptionMapper
import com.judjingm.android002.profile.data.ProfileDataMapper
import com.judjingm.android002.profile.data.api.ProfileApiService
import com.judjingm.android002.profile.data.api.ProfileRemoteDataSource
import com.judjingm.android002.profile.data.models.CreateSessionRequestDto
import com.judjingm.android002.profile.data.models.DeleteSessionDto
import com.judjingm.android002.profile.data.models.DeleteSessionRequestDto
import com.judjingm.android002.profile.data.models.RequestTokenDto
import com.judjingm.android002.profile.data.models.SessionDto
import javax.inject.Inject

class ProfileRemoteDataSourceImpl @Inject constructor(
    private val profileApi: ProfileApiService,
    private val dataMapper: ProfileDataMapper,
    private val networkToProfileExceptionMapper: NetworkToProfileExceptionMapper
) : ProfileRemoteDataSource {
    override suspend fun createRequestToken(): RequestTokenDto {
        return try {
            val response = profileApi.createRequestToken()
            dataMapper.toRequestTokenDto(response)
        } catch (exception: NetworkException) {
            throw networkToProfileExceptionMapper.handleException(exception)
        }
    }

    override suspend fun createSession(createSessionRequestDto: CreateSessionRequestDto): SessionDto {
        return try {
            val response =
                profileApi.createSession(dataMapper.toCreateSessionRequest(createSessionRequestDto))
            dataMapper.toCreateSessionDto(response)
        } catch (exception: NetworkException) {
            throw networkToProfileExceptionMapper.handleException(exception)
        }
    }

    override suspend fun deleteSession(deleteSessionRequestDto: DeleteSessionRequestDto): DeleteSessionDto {
        return try {
            val response =
                profileApi.deleteSession(dataMapper.toDeleteSessionRequest(deleteSessionRequestDto))
            dataMapper.toDeleteSessionDto(response)
        } catch (exception: NetworkException) {
            throw networkToProfileExceptionMapper.handleException(exception)
        }
    }
}