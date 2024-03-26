package com.judjingm.android002.profile.domain.useCase

import com.judjingm.android002.profile.domain.models.Authentication
import com.judjingm.android002.profile.domain.repository.CredentialsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface AuthenticationUseCase {
    suspend operator fun invoke(): Flow<Authentication>

    class Base @Inject constructor(
        private val credentialsRepository: CredentialsRepository
    ) :
        AuthenticationUseCase {
        override suspend fun invoke(): Flow<Authentication> {
            return credentialsRepository.getCredentialsFlow().map {
                if (it == null) Authentication.NotAuthenticated else Authentication.Authenticated
            }
        }
    }
}