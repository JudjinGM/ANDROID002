package com.judjingm.android002.profile.domain.repository

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.profile.domain.models.DeleteSession
import com.judjingm.android002.profile.domain.models.ProfileDetails
import com.judjingm.android002.profile.domain.models.RequestToken
import com.judjingm.android002.profile.domain.models.Session
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    fun isAuthenticationInProgress(): Flow<Boolean>
    suspend fun createRequestToken(): Resource<RequestToken, ErrorEntity>
    suspend fun createSession(): Resource<Session, ErrorEntity>
    suspend fun deleteSession(sessionId: String): Resource<DeleteSession, ErrorEntity>
    suspend fun getProfileDetails(sessionId: String): Resource<ProfileDetails, ErrorEntity>
}