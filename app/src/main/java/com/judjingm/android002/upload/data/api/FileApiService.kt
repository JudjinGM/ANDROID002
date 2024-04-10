package com.judjingm.android002.upload.data.api

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileApiService {
    @Multipart
    @POST("pdf")
    suspend fun uploadDocument(@Part document: MultipartBody.Part)
}