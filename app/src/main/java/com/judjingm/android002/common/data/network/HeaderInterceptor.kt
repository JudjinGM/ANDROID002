package com.judjingm.android002.common.data.network

import com.judjingm.android002.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeaderInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(AUTHORIZATION, "$BEARER ${BuildConfig.TMDB_ACCESS_TOKEN}")
        return chain.proceed(request.build())
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val BEARER = "Bearer"
    }

}
