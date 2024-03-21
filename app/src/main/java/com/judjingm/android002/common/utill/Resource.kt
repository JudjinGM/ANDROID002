package com.judjingm.android002.common.utill

sealed class Resource<T, E> {
    abstract suspend fun handle(handler: ResultHandler<T, E>)
    class Success<T, E>(val data: T) : Resource<T, E>() {
        override suspend fun handle(handler: ResultHandler<T, E>) {
            handler.handleSuccess(data)
        }
    }

    class Error<T, E>(val error: E) : Resource<T, E>() {
        override suspend fun handle(handler: ResultHandler<T, E>) {
            handler.handleError(error)
        }
    }


    interface ResultHandler<T, E> {
        suspend fun handleSuccess(data: T)
        suspend fun handleError(errorStatus: E)
    }
}

