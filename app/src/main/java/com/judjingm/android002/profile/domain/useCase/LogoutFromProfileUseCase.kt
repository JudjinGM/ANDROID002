package com.judjingm.android002.profile.domain.useCase

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.profile.domain.repository.AuthenticationRepository
import com.judjingm.android002.profile.domain.repository.CredentialsRepository
import javax.inject.Inject

interface LogoutFromProfileUseCase {
    suspend operator fun invoke(): Resource<Boolean, ErrorEntity>

    class Base @Inject constructor(
        private val authenticationRepository: AuthenticationRepository,
        private val credentialsRepository: CredentialsRepository,
    ) : LogoutFromProfileUseCase {
        override suspend fun invoke(): Resource<Boolean, ErrorEntity> {
            val sessionId = credentialsRepository.getCredentials()?.sessionId
            if (sessionId == null) {
                return Resource.Error(ErrorEntity.Authentication.InvalidCredentials())
            } else {
                when (val result = authenticationRepository.deleteSession(sessionId)) {
                    is Resource.Success -> {
                        credentialsRepository.clearCredentials()
                        return Resource.Success(true)
                    }

                    is Resource.Error -> {
                        return Resource.Error(result.error)
                    }
                }
            }
        }
    }

}