package com.judjingm.android002.common.utill.exceptions

import java.io.IOException

sealed class ContentDetailsException(
    override val message: String
) : IOException(message) {
    data class NoConnection(
        override val message: String = NO_INTERNET_CONNECTION
    ) : ContentDetailsException(message)

    data class Undefined(override val message: String = UNDEFINED_MESSAGE) :
        ContentDetailsException(message)

    sealed class ContentDetails(message: String) : ContentDetailsException(message) {
        class InvalidContentId(
            override val message: String,
            val statusCode: Int
        ) : ContentDetails(message)

        class InvalidAPIkey(
            override val message: String,
            val statusCode: Int
        ) : ContentDetails(message)
    }

    companion object {
        const val NO_INTERNET_CONNECTION = "No internet connection"
        const val UNDEFINED_MESSAGE = "Undefined"
    }
}