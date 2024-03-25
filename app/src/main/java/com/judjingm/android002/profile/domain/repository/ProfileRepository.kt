package com.judjingm.android002.profile.domain.repository

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.profile.domain.models.CreateSessionRequestDomain
import com.judjingm.android002.profile.domain.models.DeleteSession
import com.judjingm.android002.profile.domain.models.DeleteSessionRequestDomain
import com.judjingm.android002.profile.domain.models.RequestToken
import com.judjingm.android002.profile.domain.models.Session
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun createRequestToken(): Flow<Resource<RequestToken, ErrorEntity>>
    fun createSession(createSessionRequestDomain: CreateSessionRequestDomain): Flow<Resource<Session, ErrorEntity>>
    fun deleteSession(deleteSessionRequestDomain: DeleteSessionRequestDomain): Flow<Resource<DeleteSession, ErrorEntity>>
}