package com.judjingm.android002.common.data.network

import app.cashadvisor.common.utill.extensions.logNetworkError
import com.judjingm.android002.common.utill.exceptions.NetworkException
import java.io.IOException
import javax.inject.Inject

class NetworkErrorCodeToExceptionMapper @Inject constructor() {

    fun getException(errorMessage: String, responseCode: Int): IOException {
        logNetworkError(errorMessage)
        return when (responseCode) {
            400 -> NetworkException.BadRequest(errorMessage, responseCode)
            401 -> NetworkException.Unauthorized(errorMessage, responseCode)
            403 -> NetworkException.Forbidden(errorMessage, responseCode)
            404 -> NetworkException.NotFound(errorMessage, responseCode)
            405 -> NetworkException.MethodNotAllowed(errorMessage, responseCode)
            406 -> NetworkException.InvalidAcceptHeader(errorMessage, responseCode)
            422 -> NetworkException.UnprocessableContent(errorMessage, responseCode)
            500 -> NetworkException.InternalServerError(errorMessage, responseCode)
            501 -> NetworkException.InvalidService(errorMessage, responseCode)
            502 -> NetworkException.CouldNotConnectToTheBackendServer(errorMessage, responseCode)
            503 -> NetworkException.ServiceUnavailable(errorMessage, responseCode)
            504 -> NetworkException.GatewayTimeout(errorMessage, responseCode)

            else -> NetworkException.Undefined(errorMessage, responseCode)
        }
    }

}