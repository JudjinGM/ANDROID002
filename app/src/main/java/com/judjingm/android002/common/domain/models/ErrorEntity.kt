package com.judjingm.android002.common.domain.models

sealed class ErrorEntity(open val message: String) {
    data class UnknownError(override val message: String = BLANC_ERROR) : ErrorEntity(message)

    sealed class NetworksError(override val message: String) : ErrorEntity(message) {
        data class NoInternet(override val message: String) : NetworksError(message)
    }

    sealed class PopularContent(override val message: String) : ErrorEntity(message) {
        data class InvalidPage(override val message: String) : PopularContent(message)
        data class Unauthorized(override val message: String) : PopularContent(message)
    }

    sealed class SearchContent(override val message: String) : ErrorEntity(message) {
        data class InvalidPage(override val message: String) : SearchContent(message)
        data class Unauthorized(override val message: String) : SearchContent(message)
    }

    sealed class ContentDetail(override val message: String) : ErrorEntity(message) {
        data class Unauthorized(override val message: String) : ContentDetail(message)
    }

    companion object {
        const val BLANC_ERROR = "Unknown error"
    }
}

