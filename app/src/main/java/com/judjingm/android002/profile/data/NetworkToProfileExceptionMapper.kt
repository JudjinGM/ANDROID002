package com.judjingm.android002.profile.data

import com.judjingm.android002.common.data.models.ErrorResponse
import com.judjingm.android002.common.utill.exceptions.NetworkException
import com.judjingm.android002.common.utill.exceptions.ProfileException
import kotlinx.serialization.json.Json
import javax.inject.Inject

class NetworkToProfileExceptionMapper @Inject constructor(
    private val json: Json
) {
    fun handleException(exception: NetworkException): ProfileException {
        return when (exception) {
            is NetworkException.Unauthorized -> {
                val response = handleErrorResponse<ErrorResponse>(exception.errorBody)
                return when (response.statusCode) {
                    7 -> {
                        ProfileException.Profile.InvalidAPIkey(
                            message = response.statusMessage,
                            statusCode = response.statusCode
                        )
                    }

                    17 -> {
                        ProfileException.Profile.Denied(
                            message = response.statusMessage,
                            statusCode = response.statusCode
                        )
                    }

                    else -> {
                        ProfileException.Undefined(message = response.statusMessage)
                    }
                }
            }

            is NetworkException.NotFound -> {
                val response = handleErrorResponse<ErrorResponse>(exception.errorBody)
                ProfileException.Profile.NotFound(
                    message = response.statusMessage,
                    statusCode = response.statusCode
                )
            }

            else -> {
                handleCommonException(exception)
            }
        }
    }

    private fun handleCommonException(exception: NetworkException): ProfileException {
        return when (exception) {
            is NetworkException.NoInternetConnection -> {
                ProfileException.NoConnection(message = exception.errorBody)
            }

            is NetworkException.Undefined -> {
                val response = handleErrorResponse<ErrorResponse>(exception.errorBody)
                ProfileException.Undefined(message = response.statusMessage)
            }

            else -> {
                val response = handleErrorResponse<ErrorResponse>(exception.errorBody)
                ProfileException.Undefined(message = response.statusMessage)
            }
        }
    }

    private inline fun <reified T> handleErrorResponse(errorMessage: String): T {
        try {
            return json.decodeFromString<T>(errorMessage)

        } catch (e: Exception) {
            throw ProfileException.Undefined(COULD_NOT_CONVERT_TO_ERROR_RESPONSE)
        }
    }

    companion object {
        const val COULD_NOT_CONVERT_TO_ERROR_RESPONSE = "Couldn't convert to ErrorResponse"
    }

}