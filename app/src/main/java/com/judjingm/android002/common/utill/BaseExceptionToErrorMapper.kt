package com.judjingm.android002.common.utill

import app.cashadvisor.common.utill.extensions.logNetworkError
import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.exceptions.NetworkException
import java.net.ConnectException

abstract class BaseExceptionToErrorMapper {

    abstract fun handleSpecificException(
        exception: Exception,
    ): ErrorEntity

    fun handleException(exception: Exception): ErrorEntity {
        return when (exception) {
            is ConnectException, is NetworkException -> {
                logNetworkError(exception.message)
                handleNetworkError(exception)
            }

            else -> {
                logNetworkError(exception.message)
                handleSpecificException(exception)
            }
        }
    }

    private fun handleNetworkError(exception: Exception): ErrorEntity {
        return ErrorEntity.NetworksError.NoInternet(
            exception.message ?: DEFAULT_ERROR_MESSAGE
        )
    }

    protected fun handleUnknownError(exception: Exception): ErrorEntity {
        return ErrorEntity.UnknownError(
            exception.message ?: DEFAULT_ERROR_MESSAGE
        )
    }

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "No error message"
    }
}

