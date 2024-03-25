package com.judjingm.android002.details.data

import com.judjingm.android002.common.data.models.ErrorResponse
import com.judjingm.android002.common.utill.exceptions.ContentDetailsException
import com.judjingm.android002.common.utill.exceptions.NetworkException
import kotlinx.serialization.json.Json
import javax.inject.Inject

class NetworkToContentDetailExceptionMapper @Inject constructor(
    private val json: Json
) {
    fun handleException(exception: NetworkException): ContentDetailsException {
        return when (exception) {
            is NetworkException.BadRequest -> {
                val response = handleErrorResponse<ErrorResponse>(exception.errorBody)
                ContentDetailsException.ContentDetails.InvalidContentId(
                    message = response.statusMessage,
                    statusCode = response.statusCode
                )
            }

            is NetworkException.Unauthorized -> {
                val response = handleErrorResponse<ErrorResponse>(exception.errorBody)
                ContentDetailsException.ContentDetails.InvalidAPIkey(
                    message = response.statusMessage,
                    statusCode = response.statusCode
                )
            }

            else -> {
                handleCommonException(exception)
            }
        }
    }

    private fun handleCommonException(exception: NetworkException): ContentDetailsException {
        return when (exception) {
            is NetworkException.NoInternetConnection -> {
                ContentDetailsException.NoConnection(message = exception.errorBody)
            }

            is NetworkException.Undefined -> {
                val response = handleErrorResponse<ErrorResponse>(exception.errorBody)
                ContentDetailsException.Undefined(message = response.statusMessage)
            }

            else -> {
                val response = handleErrorResponse<ErrorResponse>(exception.errorBody)
                ContentDetailsException.Undefined(message = response.statusMessage)
            }
        }
    }

    private inline fun <reified T> handleErrorResponse(errorMessage: String): T {
        try {
            return json.decodeFromString<T>(errorMessage)

        } catch (e: Exception) {
            throw ContentDetailsException.Undefined(COULD_NOT_CONVERT_TO_ERROR_RESPONSE)
        }
    }

    companion object {
        const val COULD_NOT_CONVERT_TO_ERROR_RESPONSE = "Couldn't convert to ErrorResponse"
    }

}