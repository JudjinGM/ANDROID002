package com.judjingm.android002.profile.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.judjingm.android002.profile.data.api.CredentialsRepository
import com.judjingm.android002.profile.data.impl.CredentialsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AuthenticationDataModule {

    @Provides
    @Singleton
    fun providesMasterKey(): String =
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    @Provides
    @Singleton
    fun providesEncryptedSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val masterKey = providesMasterKey()
        return EncryptedSharedPreferences.create(
            SECRET_SETTINGS,
            masterKey,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @Singleton
    fun providesCredentialsStorage(
        @ApplicationContext context: Context,
        storage: SharedPreferences = providesEncryptedSharedPreferences(context),
        key: String = CREDENTIALS_KEY,
        gson: Json
    ): CredentialsRepository = CredentialsRepositoryImpl(storage, key, gson)

    companion object {
        private const val SECRET_SETTINGS = "secret_shared_prefs"
        private const val CREDENTIALS_KEY = "secret_credentials"
    }
}