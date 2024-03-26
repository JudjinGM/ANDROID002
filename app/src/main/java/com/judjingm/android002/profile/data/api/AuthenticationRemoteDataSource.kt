package com.judjingm.android002.profile.data.api

import com.judjingm.android002.profile.data.models.CreateSessionRequestDto
import com.judjingm.android002.profile.data.models.DeleteSessionDto
import com.judjingm.android002.profile.data.models.DeleteSessionRequestDto
import com.judjingm.android002.profile.data.models.ProfileDetailsDto
import com.judjingm.android002.profile.data.models.RequestTokenDto
import com.judjingm.android002.profile.data.models.SessionDto

interface AuthenticationRemoteDataSource {
    suspend fun createRequestToken(): RequestTokenDto
    suspend fun createSession(createSessionRequestDto: CreateSessionRequestDto): SessionDto
    suspend fun deleteSession(deleteSessionRequestDto: DeleteSessionRequestDto): DeleteSessionDto
    suspend fun getProfileDetails(sessionId: String): ProfileDetailsDto
}