package com.judjingm.android002.profile.domain

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.BaseExceptionToErrorEntityMapper
import com.judjingm.android002.common.utill.exceptions.ProfileException
import javax.inject.Inject

class ProfileExceptionToErrorEntityMapper @Inject constructor(
) : BaseExceptionToErrorEntityMapper() {
    override fun handleSpecificException(
        exception: Exception,
    ): ErrorEntity {
        return when (exception) {
            is ProfileException.Profile -> handleProfileException(exception)
            else -> handleUnknownError(exception)
        }
    }

    private fun handleProfileException(
        exception: ProfileException.Profile
    ): ErrorEntity {
        return when (exception) {
            is ProfileException.Profile.Denied,
            is ProfileException.Profile.InvalidAPIkey,
            is ProfileException.Profile.NotFound -> ErrorEntity.Profile.Unauthorized(exception.message)
        }
    }
}