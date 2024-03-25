package com.judjingm.android002.details.domain.useCase

import com.judjingm.android002.details.data.api.SystemLocalProvider
import javax.inject.Inject

interface GetLanguageForAppUseCase {
    operator fun invoke(): String
    class Base @Inject constructor(private val systemLocalProvider: SystemLocalProvider) :
        GetLanguageForAppUseCase {
        override fun invoke(): String {
            return systemLocalProvider.getLanguage()
        }
    }
}