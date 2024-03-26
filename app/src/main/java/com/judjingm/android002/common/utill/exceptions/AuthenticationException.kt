package com.judjingm.android002.common.utill.exceptions

import java.io.IOException

sealed class AuthenticationException(
    override val message: String
) : IOException(message) {
    data class NoConnection(
        override val message: String = NO_INTERNET_CONNECTION
    ) : AuthenticationException(message)

    data class Undefined(override val message: String = UNDEFINED_MESSAGE) :
        AuthenticationException(message)

    sealed class Authentication(message: String) : AuthenticationException(message) {
        class InvalidAPIkey(
            override val message: String,
            val statusCode: Int
        ) : Authentication(message)

        class NotFound(
            override val message: String,
            val statusCode: Int
        ) : Authentication(message)

        class Denied(
            override val message: String,
            val statusCode: Int
        ) : Authentication(message)
    }

    companion object {
        const val NO_INTERNET_CONNECTION = "No internet connection"
        const val UNDEFINED_MESSAGE = "Undefined"
    }

}