package com.judjingm.android002.common.utill.exceptions

import java.io.IOException

sealed class ProfileException(
    override val message: String
) : IOException(message) {
    data class NoConnection(
        override val message: String = NO_INTERNET_CONNECTION
    ) : ProfileException(message)

    data class Undefined(override val message: String = UNDEFINED_MESSAGE) :
        ProfileException(message)

    sealed class Profile(message: String) : ProfileException(message) {
        class InvalidAPIkey(
            override val message: String,
            val statusCode: Int
        ) : Profile(message)

        class NotFound(
            override val message: String,
            val statusCode: Int
        ) : Profile(message)

        class Denied(
            override val message: String,
            val statusCode: Int
        ) : Profile(message)
    }

    companion object {
        const val NO_INTERNET_CONNECTION = "No internet connection"
        const val UNDEFINED_MESSAGE = "Undefined"
    }

}