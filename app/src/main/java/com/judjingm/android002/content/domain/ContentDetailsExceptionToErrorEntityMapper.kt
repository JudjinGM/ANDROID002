package com.judjingm.android002.content.domain

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.BaseExceptionToErrorEntityMapper
import com.judjingm.android002.common.utill.exceptions.ContentDetailsException
import javax.inject.Inject

class ContentDetailsExceptionToErrorEntityMapper @Inject constructor(
) : BaseExceptionToErrorEntityMapper() {
    override fun handleSpecificException(
        exception: Exception,
    ): ErrorEntity {
        return when (exception) {
            is ContentDetailsException -> handleContentDetailsException(exception)
            else -> handleUnknownError(exception)
        }
    }

    private fun handleContentDetailsException(
        exception: ContentDetailsException,
    ): ErrorEntity {
        return when (exception) {
            is ContentDetailsException.ContentDetails.InvalidAPIkey -> {
                ErrorEntity.ContentDetail.Unauthorized(exception.message)
            }

            is ContentDetailsException.ContentDetails.InvalidContentId -> {
                ErrorEntity.UnknownError(exception.message)
            }

            is ContentDetailsException.NoConnection -> {
                ErrorEntity.NetworksError.NoInternet(exception.message)
            }

            is ContentDetailsException.Undefined -> {
                ErrorEntity.UnknownError(exception.message)
            }
        }
    }
}