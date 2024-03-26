package com.judjingm.android002.profile.data

import com.judjingm.android002.common.data.models.ErrorResponse
import com.judjingm.android002.common.utill.exceptions.AuthenticationException
import com.judjingm.android002.common.utill.exceptions.NetworkException
import kotlinx.serialization.json.Json
import javax.inject.Inject

class NetworkToAuthenticationExceptionMapper @Inject constructor(
    private val json: Json
) {
    fun handleException(exception: NetworkException): AuthenticationException {
        return when (exception) {
            is NetworkException.Unauthorized -> {
                val response = handleErrorResponse<ErrorResponse>(exception.errorBody)
                return when (response.statusCode) {
                    7 -> {
                        AuthenticationException.Authentication.InvalidAPIkey(
                            message = response.statusMessage,
                            statusCode = response.statusCode
                        )
                    }

                    17 -> {
                        AuthenticationException.Authentication.Denied(
                            message = response.statusMessage,
                            statusCode = response.statusCode
                        )
                    }

                    else -> {
                        AuthenticationException.Undefined(message = response.statusMessage)
                    }
                }
            }

            is NetworkException.NotFound -> {
                val response = handleErrorResponse<ErrorResponse>(exception.errorBody)
                AuthenticationException.Authentication.NotFound(
                    message = response.statusMessage,
                    statusCode = response.statusCode
                )
            }

            else -> {
                handleCommonException(exception)
            }
        }
    }

    private fun handleCommonException(exception: NetworkException): AuthenticationException {
        return when (exception) {
            is NetworkException.NoInternetConnection -> {
                AuthenticationException.NoConnection(message = exception.errorBody)
            }

            is NetworkException.Undefined -> {
                val response = handleErrorResponse<ErrorResponse>(exception.errorBody)
                AuthenticationException.Undefined(message = response.statusMessage)
            }

            else -> {
                val response = handleErrorResponse<ErrorResponse>(exception.errorBody)
                AuthenticationException.Undefined(message = response.statusMessage)
            }
        }
    }

    private inline fun <reified T> handleErrorResponse(errorMessage: String): T {
        try {
            return json.decodeFromString<T>(errorMessage)

        } catch (e: Exception) {
            throw AuthenticationException.Undefined(COULD_NOT_CONVERT_TO_ERROR_RESPONSE)
        }
    }

    companion object {
        const val COULD_NOT_CONVERT_TO_ERROR_RESPONSE = "Couldn't convert to ErrorResponse"
    }

}