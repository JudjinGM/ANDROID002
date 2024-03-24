package com.judjingm.android002.common.utill.exceptions

import java.io.IOException

sealed class SearchContentException(
    override val message: String
) : IOException(message) {

    data class NoConnection(
        override val message: String = NO_INTERNET_CONNECTION
    ) : SearchContentException(message)

    data class Undefined(override val message: String = UNDEFINED_MESSAGE) :
        SearchContentException(message)

    sealed class SearchContent(
        message: String
    ) : SearchContentException(message) {

        class InvalidPage(
            override val message: String,
            val statusCode: Int
        ) : SearchContent(message)

        class InvalidAPIkey(
            override val message: String,
            val statusCode: Int
        ) : SearchContent(message)
    }

    companion object {
        const val NO_INTERNET_CONNECTION = "No internet connection"
        const val UNDEFINED_MESSAGE = "Undefined"
    }

}