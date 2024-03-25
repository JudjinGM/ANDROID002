package com.judjingm.android002.profile.data

import com.judjingm.android002.profile.data.models.CreateSessionRequestDto
import com.judjingm.android002.profile.data.models.DeleteSessionDto
import com.judjingm.android002.profile.data.models.DeleteSessionRequestDto
import com.judjingm.android002.profile.data.models.RequestTokenDto
import com.judjingm.android002.profile.data.models.SessionDto
import com.judjingm.android002.profile.data.models.request.CreateSessionRequest
import com.judjingm.android002.profile.data.models.request.DeleteSessionRequest
import com.judjingm.android002.profile.data.models.response.CreateSessionResponse
import com.judjingm.android002.profile.data.models.response.DeleteSessionResponse
import com.judjingm.android002.profile.data.models.response.RequestTokenResponse
import javax.inject.Inject

class ProfileDataMapper @Inject constructor() {
    fun toCreateSessionDto(createSession: CreateSessionResponse): SessionDto {
        return SessionDto(
            success = createSession.success,
            sessionId = createSession.sessionId
        )
    }

    fun toRequestTokenDto(requestTokenResponse: RequestTokenResponse): RequestTokenDto {
        return RequestTokenDto(
            success = requestTokenResponse.success,
            expiresAt = requestTokenResponse.expiresAt,
            requestToken = requestTokenResponse.requestToken
        )
    }

    fun toCreateSessionRequest(createSessionRequestDto: CreateSessionRequestDto): CreateSessionRequest {
        return CreateSessionRequest(
            requestToken = createSessionRequestDto.requestToken
        )
    }

    fun toDeleteSessionRequest(deleteSessionRequestDto: DeleteSessionRequestDto): DeleteSessionRequest {
        return DeleteSessionRequest(
            sessionId = deleteSessionRequestDto.sessionId
        )
    }

    fun toDeleteSessionDto(deleteSessionResponse: DeleteSessionResponse): DeleteSessionDto {
        return DeleteSessionDto(
            success = deleteSessionResponse.success
        )
    }
}