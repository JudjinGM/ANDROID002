package com.judjingm.android002.profile.domain

import com.judjingm.android002.profile.data.models.CreateSessionRequestDto
import com.judjingm.android002.profile.data.models.DeleteSessionDto
import com.judjingm.android002.profile.data.models.DeleteSessionRequestDto
import com.judjingm.android002.profile.data.models.ProfileDetailsDto
import com.judjingm.android002.profile.data.models.RequestTokenDto
import com.judjingm.android002.profile.data.models.SessionDto
import com.judjingm.android002.profile.domain.models.DeleteSession
import com.judjingm.android002.profile.domain.models.ProfileDetails
import com.judjingm.android002.profile.domain.models.RequestToken
import com.judjingm.android002.profile.domain.models.Session
import javax.inject.Inject

class AuthenticationDomainMapper @Inject constructor() {
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

    fun toCreateSessionRequestDto(requestToken: String): CreateSessionRequestDto {
        return CreateSessionRequestDto(
            requestToken = requestToken,
        )
    }

    fun toDeleteSessionRequestDto(sessionId: String): DeleteSessionRequestDto {
        return DeleteSessionRequestDto(
            sessionId = sessionId
        )
    }

    fun createAuthenticationUrl(requestToken: String): String {
        return BASE_URL + requestToken
    }

    fun toProfileDetails(profileDetailsDto: ProfileDetailsDto): ProfileDetails {
        return ProfileDetails(
            id = profileDetailsDto.id,
            userName = profileDetailsDto.name,
        )
    }

    companion object {
        const val BASE_URL = "https://www.themoviedb.org/authenticate/"
    }
}