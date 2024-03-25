package com.judjingm.android002.profile.data.api

import com.judjingm.android002.profile.data.models.request.CreateSessionRequest
import com.judjingm.android002.profile.data.models.request.DeleteSessionRequest
import com.judjingm.android002.profile.data.models.response.CreateSessionResponse
import com.judjingm.android002.profile.data.models.response.DeleteSessionResponse
import com.judjingm.android002.profile.data.models.response.RequestTokenResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileApiService {
    @GET("/3/authentication/token/new")
    suspend fun createRequestToken(): RequestTokenResponse

    @POST("/3/authentication/session/new")
    suspend fun createSession(
        @Body createSessionRequest: CreateSessionRequest
    ): CreateSessionResponse

    @DELETE("/3/authentication/session")
    suspend fun deleteSession(
        @Body deleteSessionRequest: DeleteSessionRequest
    ): DeleteSessionResponse

}