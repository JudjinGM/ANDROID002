package com.judjingm.android002.profile.domain.useCase

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.profile.data.models.CredentialsDto
import com.judjingm.android002.profile.domain.repository.AuthenticationRepository
import com.judjingm.android002.profile.domain.repository.CredentialsRepository
import javax.inject.Inject

interface ConfirmLoginToProfileUseCase {
    suspend operator fun invoke(): Resource<Boolean, ErrorEntity>

    class Base @Inject constructor(
        private val authenticationRepository: AuthenticationRepository,
        private val credentialsRepository: CredentialsRepository
    ) : ConfirmLoginToProfileUseCase {
        override suspend fun invoke(): Resource<Boolean, ErrorEntity> {
            return when (val result = authenticationRepository.createSession()) {
                is Resource.Success -> {
                    credentialsRepository.saveCredentials(CredentialsDto(sessionId = result.data.sessionId))
                    Resource.Success(data = result.data.success)
                }

                is Resource.Error -> {
                    Resource.Error(error = result.error)
                }
            }
        }
    }

}