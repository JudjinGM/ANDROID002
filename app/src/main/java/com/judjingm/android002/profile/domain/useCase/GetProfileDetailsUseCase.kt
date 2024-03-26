package com.judjingm.android002.profile.domain.useCase

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.profile.domain.models.ProfileDetails
import com.judjingm.android002.profile.domain.repository.AuthenticationRepository
import com.judjingm.android002.profile.domain.repository.CredentialsRepository
import javax.inject.Inject

interface GetProfileDetailsUseCase {
    suspend operator fun invoke(): Resource<ProfileDetails, ErrorEntity>

    class Base @Inject constructor(
        private val authenticationRepository: AuthenticationRepository,
        private val credentialsRepository: CredentialsRepository
    ) : GetProfileDetailsUseCase {
        override suspend fun invoke(): Resource<ProfileDetails, ErrorEntity> {
            val token = credentialsRepository.getCredentials()?.sessionId
            if (token == null) {
                return Resource.Error(ErrorEntity.Authentication.InvalidCredentials())
            } else {
                val result = authenticationRepository.getProfileDetails(sessionId = token)
                return when (result) {
                    is Resource.Success -> {
                        Resource.Success(result.data)
                    }

                    is Resource.Error -> {
                        Resource.Error(result.error)
                    }
                }
            }
        }
    }

}