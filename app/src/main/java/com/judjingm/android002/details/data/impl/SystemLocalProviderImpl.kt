package com.judjingm.android002.details.data.impl

import android.content.Context
import android.os.Build
import com.judjingm.android002.details.data.api.SystemLocalProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SystemLocalProviderImpl @Inject constructor(@ApplicationContext private val context: Context) :
    SystemLocalProvider {
    override fun getLanguage(): String {
        val locale =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.resources.configuration.getLocales().get(0)
            } else {
                context.resources.configuration.locale
            }
        return locale.language
    }
}