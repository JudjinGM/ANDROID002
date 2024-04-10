package com.judjingm.android002.upload.data

import com.judjingm.android002.common.utill.exceptions.NetworkException
import com.judjingm.android002.common.utill.exceptions.UploadFileException
import javax.inject.Inject

class NetworkToUploadFileExceptionMapper @Inject constructor() {
    fun handleException(exception: NetworkException): UploadFileException {
        return when (exception) {
            is NetworkException.InternalServerError -> {
                UploadFileException.UploadFile.ServerError(message = exception.errorBody)
            }

            else -> {
                handleCommonException(exception)
            }
        }
    }

    private fun handleCommonException(exception: NetworkException): UploadFileException {
        return when (exception) {
            is NetworkException.NoInternetConnection -> {
                UploadFileException.NoConnection(message = exception.errorBody)
            }

            is NetworkException.Undefined -> {
                UploadFileException.Undefined(message = exception.errorBody)
            }

            else -> {
                UploadFileException.Undefined(message = exception.errorBody)
            }
        }
    }

}