package com.judjingm.android002.common.utill.exceptions

import java.io.IOException

sealed class UploadFileException(
    override val message: String
) : IOException(message) {
    data class NoConnection(
        override val message: String = NO_INTERNET_CONNECTION
    ) : UploadFileException(message)

    data class Undefined(override val message: String = UNDEFINED_MESSAGE) :
        UploadFileException(message)

    sealed class UploadFile(message: String) : UploadFileException(message) {
        class ServerError(
            override val message: String,
        ) : UploadFile(message)
    }

    companion object {
        const val NO_INTERNET_CONNECTION = "No internet connection"
        const val UNDEFINED_MESSAGE = "Undefined"
    }

}