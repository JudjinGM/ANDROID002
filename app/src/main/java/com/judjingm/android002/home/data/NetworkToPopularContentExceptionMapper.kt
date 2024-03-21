package com.judjingm.android002.home.data

import com.judjingm.android002.common.data.models.ErrorResponse
import com.judjingm.android002.common.utill.exceptions.NetworkException
import com.judjingm.android002.common.utill.exceptions.PopularContentException
import kotlinx.serialization.json.Json
import javax.inject.Inject

class NetworkToPopularContentExceptionMapper @Inject constructor(
    private val json: Json
) {
    fun handleException(exception: NetworkException): PopularContentException {
        return when (exception) {
            is NetworkException.BadRequest -> {
                val response = handleErrorResponse<ErrorResponse>(exception.errorBody)
                PopularContentException.PopularContent.InvalidPage(
                    message = response.statusMessage,
                    statusCode = response.statusCode
                )
            }

            is NetworkException.Unauthorized -> {
                val response = handleErrorResponse<ErrorResponse>(exception.errorBody)
                PopularContentException.PopularContent.InvalidAPIkey(
                    message = response.statusMessage,
                    statusCode = response.statusCode
                )
            }

            else -> {
                handleCommonException(exception)
            }
        }
    }

    private fun handleCommonException(exception: NetworkException): PopularContentException {
        return when (exception) {
            is NetworkException.NoInternetConnection -> {
                PopularContentException.NoConnection(message = exception.errorBody)
            }

            is NetworkException.Undefined -> {
                val response = handleErrorResponse<ErrorResponse>(exception.errorBody)
                PopularContentException.Undefined(message = response.statusMessage)
            }

            else -> {
                val response = handleErrorResponse<ErrorResponse>(exception.errorBody)
                PopularContentException.Undefined(message = response.statusMessage)
            }
        }
    }

    private inline fun <reified T> handleErrorResponse(errorMessage: String): T {
        try {
            return json.decodeFromString<T>(errorMessage)

        } catch (e: Exception) {
            throw PopularContentException.Undefined(COULD_NOT_CONVERT_TO_ERROR_RESPONSE)
        }
    }

    companion object {
        const val COULD_NOT_CONVERT_TO_ERROR_RESPONSE = "Couldn't convert to ErrorResponse"
    }
}