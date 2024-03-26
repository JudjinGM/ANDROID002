package com.judjingm.android002.profile.di

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.judjingm.android002.profile.data.impl.CredentialsRepositoryImpl
import com.judjingm.android002.profile.domain.repository.CredentialsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EncryptedPrefs

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RegularPrefs

@Module
@InstallIn(SingletonComponent::class)
class AuthenticationDataModule {

    @RequiresApi(23)
    @Provides
    @Singleton
    @EncryptedPrefs
    fun providesEncryptedSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
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
    @RegularPrefs
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SECRET_SETTINGS, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providesCredentialsStorage(
        @ApplicationContext context: Context,
        @RegularPrefs sharedPreferences: SharedPreferences,
        @EncryptedPrefs encryptedSharedPreferences: SharedPreferences,
        gson: Json
    ): CredentialsRepository {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            CredentialsRepositoryImpl(encryptedSharedPreferences, CREDENTIALS_KEY_ENCRYPTED, gson)
        } else {
            CredentialsRepositoryImpl(sharedPreferences, CREDENTIALS_KEY, gson)
        }
    }

    companion object {
        private const val SECRET_SETTINGS = "secret_shared_prefs"
        private const val CREDENTIALS_KEY = "secret_credentials"
        private const val CREDENTIALS_KEY_ENCRYPTED = "secret_credentials_encrypted"
    }
}
