package app.cashadvisor.common.utill.extensions

import android.util.Log

fun logNetworkError(message: String?) {
    Log.d(NETWORK_ERROR_LOG, message ?: MESSAGE_IS_NULL)
}

fun logDebugError(message: String?) {
    Log.d(ERROR_LOG, message ?: MESSAGE_IS_NULL)
}

fun logDebugMessage(message: String?) {
    Log.d(DEBUG_MESSAGE, message ?: MESSAGE_IS_NULL)
}

const val NETWORK_ERROR_LOG = "NETWORK_ERROR_LOG"
const val ERROR_LOG = "ERROR_LOG"
const val DEBUG_MESSAGE = "DEBUG_MESSAGE"
const val MESSAGE_IS_NULL = "MESSAGE_IS_NULL"