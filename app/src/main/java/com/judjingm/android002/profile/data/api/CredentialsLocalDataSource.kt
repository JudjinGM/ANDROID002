package com.judjingm.android002.profile.data.api

import com.judjingm.android002.profile.data.models.CredentialsDto
import kotlinx.coroutines.flow.Flow

interface CredentialsLocalDataSource {
    suspend fun saveCredentials(credentialsDto: CredentialsDto)
    suspend fun getCredentials(): CredentialsDto?
    fun getCredentialsFlow(): Flow<CredentialsDto?>
    suspend fun hasCredentials(): Boolean
    suspend fun clearCredentials()
}