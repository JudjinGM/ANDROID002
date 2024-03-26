package com.judjingm.android002.profile.domain.useCase

import com.judjingm.android002.profile.domain.repository.CredentialsRepository
import javax.inject.Inject

interface DeleteInvalidCredentialsUseCase {
    suspend operator fun invoke()
    class Base @Inject constructor(
        private val credentialsRepository: CredentialsRepository,
    ) : DeleteInvalidCredentialsUseCase {
        override suspend fun invoke() {
            credentialsRepository.clearCredentials()
        }
    }
}