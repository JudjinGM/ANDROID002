package com.judjingm.android002.profile.domain

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.BaseExceptionToErrorEntityMapper
import com.judjingm.android002.common.utill.exceptions.AuthenticationException
import javax.inject.Inject

class AuthenticationExceptionToErrorEntityMapper @Inject constructor(
) : BaseExceptionToErrorEntityMapper() {
    override fun handleSpecificException(
        exception: Exception,
    ): ErrorEntity {
        return when (exception) {
            is AuthenticationException -> handleProfileException(exception)
            else -> handleUnknownError(exception)
        }
    }

    private fun handleProfileException(
        exception: AuthenticationException
    ): ErrorEntity {
        return when (exception) {
            is AuthenticationException.Authentication.Denied,
            is AuthenticationException.Authentication.InvalidAPIkey,
            is AuthenticationException.Authentication.NotFound -> {
                ErrorEntity.Authentication.Unauthorized(
                    exception.message
                )
            }

            is AuthenticationException.NoConnection -> {
                ErrorEntity.NetworksError.NoInternet(
                    exception.message
                )
            }

            is AuthenticationException.Undefined -> {
                ErrorEntity.UnknownError(exception.message)
            }
        }
    }
}