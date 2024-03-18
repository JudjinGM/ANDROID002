package com.judjingm.android002.common.utill

sealed class Resource<T, E>(val data: T? = null, val error: E? = null) {
    abstract fun handle(handler: ResultHandler)
    class Success<T, E>(data: T) : Resource<T, E>(data = data) {
        override fun handle(handler: ResultHandler) {
            handler.handleSuccess(data)
        }

    }

    class Error<T, E>(error: E) : Resource<T, E>(error = error) {
        override fun handle(handler: ResultHandler) {
            handler.handleError(error)
        }
    }


    interface ResultHandler {
        fun <T> handleSuccess(data: T)
        fun <E> handleError(errorStatus: E)
    }
}

