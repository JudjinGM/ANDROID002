package com.judjingm.android002.profile.data.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.judjingm.android002.profile.data.models.CredentialsDto
import com.judjingm.android002.profile.domain.repository.CredentialsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class CredentialsRepositoryImpl @Inject constructor(
    private val storage: SharedPreferences,
    private val key: String,
    private val json: Json
) : CredentialsRepository, CoroutineScope by CoroutineScope(
    Dispatchers.IO +
            SupervisorJob()
) {

    private val _credentialsUpdateFlow = MutableStateFlow<CredentialsDto?>(null)

    init {
        storage.registerOnSharedPreferenceChangeListener(this)
        launch {
            _credentialsUpdateFlow.value = getCredentials()
        }
    }

    override suspend fun saveCredentials(credentialsDto: CredentialsDto) {
        withContext(Dispatchers.IO) {
            val data = json.encodeToString(credentialsDto)
            storage.edit { putString(key, data) }
        }
    }

    override suspend fun getCredentials(): CredentialsDto? {
        return withContext(Dispatchers.IO) {
            storage.getString(key, null)?.let {
                json.decodeFromString<CredentialsDto>(it)
            }
        }
    }

    override fun getCredentialsFlow(): Flow<CredentialsDto?> = _credentialsUpdateFlow

    override suspend fun hasCredentials(): Boolean {
        return withContext(Dispatchers.IO) {
            storage.contains(key)
        }
    }

    override suspend fun clearCredentials() {
        withContext(Dispatchers.IO) {
            storage.edit { remove(key) }
            _credentialsUpdateFlow.emit(null)
        }
    }

    override fun onSharedPreferenceChanged(
        p0: SharedPreferences?,
        p1: String?
    ) {
        if (p1 == key) {
            launch {
                _credentialsUpdateFlow.value = getCredentials()
            }
        }
    }
}