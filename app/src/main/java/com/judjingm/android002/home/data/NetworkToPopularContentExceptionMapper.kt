package com.judjingm.android002.home.data

import com.judjingm.android002.common.data.models.ErrorResponse
import com.judjingm.android002.common.utill.exceptions.NetworkException
import com.judjingm.android002.common.utill.exceptions.PopularContentException
import kotlinx.serialization.json.Json

class NetworkToPopularContentExceptionMapper(
    private val json: Json
) {
    fun handleException(exception: NetworkException): PopularContentException {
        return when (exception) {
            is NetworkException.BadRequest -> {
                PopularContentException.PopularContent.InvalidPage(
                    message = exception.errorBody,
                    statusCode = exception.httpStatusCode
                )
            }

            is NetworkException.Unauthorized -> {
                PopularContentException.PopularContent.InvalidAPIkey(
                    message = exception.errorBody,
                    statusCode = exception.httpStatusCode
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
                val errorResponse = handleErrorResponse<ErrorResponse>(exception.errorBody)
                PopularContentException.NoConnection(errorResponse.statusMessage)
            }

            is NetworkException.Undefined -> {
                PopularContentException.Undefined(message = exception.errorBody)
            }

            else -> {
                PopularContentException.Undefined(message = exception.errorBody)
            }
        }
    }

    private inline fun <reified T> handleErrorResponse(errorMessage: String): T {
        try {
            return json.decodeFromString<T>(errorMessage)

        } catch (e: Exception) {
            throw PopularContentException.Undefined()
        }
    }
}