package com.judjingm.android002.profile.data.api

import com.judjingm.android002.profile.data.models.request.CreateSessionRequest
import com.judjingm.android002.profile.data.models.response.CreateSessionResponse
import com.judjingm.android002.profile.data.models.response.DeleteSessionResponse
import com.judjingm.android002.profile.data.models.response.ProfileDetailsResponse
import com.judjingm.android002.profile.data.models.response.RequestTokenResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationApiService {
    @GET("/3/authentication/token/new")
    suspend fun createRequestToken(): RequestTokenResponse

    @POST("/3/authentication/session/new")
    suspend fun createSession(
        @Body createSessionRequest: CreateSessionRequest
    ): CreateSessionResponse

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/3/authentication/session", hasBody = true)
    suspend fun deleteSession(
        @Field("session_id") sessionId: String
    ): DeleteSessionResponse

    @GET("/3/account")
    suspend fun getProfileDetails(@Query("session_id") sessionId: String): ProfileDetailsResponse

}