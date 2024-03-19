package com.judjingm.android002.home.domain

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.BaseExceptionToErrorMapper
import com.judjingm.android002.common.utill.exceptions.PopularContentException
import javax.inject.Inject

class PopularContentExceptionToErrorMapper @Inject constructor(
) : BaseExceptionToErrorMapper() {

    override fun handleSpecificException(
        exception: Exception,
    ): ErrorEntity {
        return when (exception) {
            is PopularContentException.PopularContent -> handlePopularContentException(exception)
            else -> handleUnknownError(exception)
        }
    }

    private fun handlePopularContentException(exception: PopularContentException.PopularContent): ErrorEntity {
        return when (exception) {
            is PopularContentException.PopularContent.InvalidAPIkey -> {
                ErrorEntity.PopularContent.Unauthorized(exception.message)
            }

            is PopularContentException.PopularContent.InvalidPage -> {
                ErrorEntity.PopularContent.InvalidPage(exception.message)
            }
        }
    }

}
