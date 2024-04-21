package com.judjingm.android002.profile.data.impl

import com.judjingm.android002.profile.data.api.CredentialsLocalDataSource
import com.judjingm.android002.profile.data.models.CredentialsDto
import com.judjingm.android002.profile.domain.repository.CredentialsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CredentialsRepositoryImpl @Inject constructor(
    private val localDataSource: CredentialsLocalDataSource
) : CredentialsRepository {
    override suspend fun saveCredentials(credentialsDto: CredentialsDto) {
        localDataSource.saveCredentials(credentialsDto)
    }

    override suspend fun getCredentials(): CredentialsDto? {
        return localDataSource.getCredentials()
    }

    override fun getCredentialsFlow(): Flow<CredentialsDto?> {
        return localDataSource.getCredentialsFlow()
    }

    override suspend fun hasCredentials(): Boolean {
        return localDataSource.hasCredentials()
    }

    override suspend fun clearCredentials() {
        localDataSource.clearCredentials()
    }
}