package com.judjingm.android002.profile.domain.useCase

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.profile.domain.AuthenticationDomainMapper
import com.judjingm.android002.profile.domain.models.LoginData
import com.judjingm.android002.profile.domain.repository.AuthenticationRepository
import javax.inject.Inject

interface RequestLoginToProfileUseCase {
    suspend operator fun invoke(): Resource<LoginData, ErrorEntity>

    class Base @Inject constructor(
        private val authenticationRepository: AuthenticationRepository,
        private val authenticationDomainMapper: AuthenticationDomainMapper
    ) : RequestLoginToProfileUseCase {
        override suspend fun invoke(): Resource<LoginData, ErrorEntity> {
            return when (val result = authenticationRepository.createRequestToken()) {
                is Resource.Success -> {
                    Resource.Success(
                        LoginData(
                            success = result.data.success,
                            urlToProceed = authenticationDomainMapper
                                .createAuthenticationUrl(result.data.requestToken)
                        )
                    )
                }

                is Resource.Error -> {
                    Resource.Error(result.error)
                }
            }
        }
    }
}