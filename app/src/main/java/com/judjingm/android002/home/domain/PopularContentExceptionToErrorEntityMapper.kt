package com.judjingm.android002.home.domain

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.BaseExceptionToErrorEntityMapper
import com.judjingm.android002.common.utill.exceptions.PopularContentException
import javax.inject.Inject

class PopularContentExceptionToErrorEntityMapper @Inject constructor(
) : BaseExceptionToErrorEntityMapper() {

    override fun handleSpecificException(
        exception: Exception,
    ): ErrorEntity {
        return when (exception) {
            is PopularContentException -> handlePopularContentException(exception)
            else -> handleUnknownError(exception)
        }
    }

    private fun handlePopularContentException(exception: PopularContentException): ErrorEntity {
        return when (exception) {
            is PopularContentException.PopularContent.InvalidAPIkey -> {
                ErrorEntity.PopularContent.Unauthorized(exception.message)
            }

            is PopularContentException.PopularContent.InvalidPage -> {
                ErrorEntity.PopularContent.InvalidPage(exception.message)
            }

            is PopularContentException.NoConnection -> {
                ErrorEntity.NetworksError.NoInternet(exception.message)
            }

            is PopularContentException.Undefined -> {
                ErrorEntity.UnknownError(exception.message)
            }
        }
    }

}
