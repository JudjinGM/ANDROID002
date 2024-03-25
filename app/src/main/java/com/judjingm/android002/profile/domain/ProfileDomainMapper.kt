package com.judjingm.android002.profile.domain

import com.judjingm.android002.profile.data.models.CreateSessionRequestDto
import com.judjingm.android002.profile.data.models.DeleteSessionDto
import com.judjingm.android002.profile.data.models.DeleteSessionRequestDto
import com.judjingm.android002.profile.data.models.RequestTokenDto
import com.judjingm.android002.profile.data.models.SessionDto
import com.judjingm.android002.profile.domain.models.CreateSessionRequestDomain
import com.judjingm.android002.profile.domain.models.DeleteSession
import com.judjingm.android002.profile.domain.models.DeleteSessionRequestDomain
import com.judjingm.android002.profile.domain.models.RequestToken
import com.judjingm.android002.profile.domain.models.Session
import javax.inject.Inject

class ProfileDomainMapper @Inject constructor() {
    fun toCreateSession(sessionDto: SessionDto): Session {
        return Session(
            success = sessionDto.success,
            sessionId = sessionDto.sessionId,
        )
    }

    fun toDeleteSession(deleteSessionDto: DeleteSessionDto): DeleteSession {
        return DeleteSession(
            success = deleteSessionDto.success,
        )
    }

    fun toRequestToken(requestTokenDto: RequestTokenDto): RequestToken {
        return RequestToken(
            success = requestTokenDto.success,
            expiresAt = requestTokenDto.expiresAt,
            requestToken = requestTokenDto.requestToken,
        )
    }

    fun toCreateSessionRequestDto(createSessionRequestDomain: CreateSessionRequestDomain): CreateSessionRequestDto {
        return CreateSessionRequestDto(
            requestToken = createSessionRequestDomain.requestToken,
        )
    }

    fun toDeleteSessionRequestDto(deleteSessionRequestDomain: DeleteSessionRequestDomain): DeleteSessionRequestDto {
        return DeleteSessionRequestDto(
            sessionId = deleteSessionRequestDomain.sessionId,
        )
    }
}