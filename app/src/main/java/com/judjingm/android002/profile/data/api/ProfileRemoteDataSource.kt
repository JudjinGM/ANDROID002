package com.judjingm.android002.profile.data.api

import com.judjingm.android002.profile.data.models.CreateSessionRequestDto
import com.judjingm.android002.profile.data.models.DeleteSessionDto
import com.judjingm.android002.profile.data.models.DeleteSessionRequestDto
import com.judjingm.android002.profile.data.models.RequestTokenDto
import com.judjingm.android002.profile.data.models.SessionDto

interface ProfileRemoteDataSource {
    suspend fun createRequestToken(): RequestTokenDto
    suspend fun createSession(createSessionRequestDto: CreateSessionRequestDto): SessionDto
    suspend fun deleteSession(deleteSessionRequestDto: DeleteSessionRequestDto): DeleteSessionDto
}