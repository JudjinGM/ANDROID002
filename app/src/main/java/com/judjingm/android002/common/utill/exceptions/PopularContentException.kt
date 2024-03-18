package com.judjingm.android002.common.utill.exceptions

import java.io.IOException

sealed class PopularContentException(
    override val message: String
) : IOException(message) {

    data class NoConnection(
        override val message: String = NO_INTERNET_CONNECTION
    ) : PopularContentException(message)

    data class Undefined(override val message: String = UNDEFINED_MESSAGE) :
        PopularContentException(message)

    sealed class PopularContent(
        message: String
    ) : PopularContentException(message) {

        class InvalidPage(
            override val message: String,
            val statusCode: Int
        ) : PopularContent(message)

        class InvalidAPIkey(
            override val message: String,
            val statusCode: Int
        ) : PopularContent(message)
    }

    companion object {
        const val NO_INTERNET_CONNECTION = "No internet connection"
        const val UNDEFINED_MESSAGE = "Undefined"
    }

}