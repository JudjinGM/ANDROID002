package com.judjingm.android002.search.domain

import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.utill.BaseExceptionToErrorEntityMapper
import com.judjingm.android002.common.utill.exceptions.SearchContentException
import javax.inject.Inject

class SearchContentExceptionToErrorEntityMapper @Inject constructor(
) : BaseExceptionToErrorEntityMapper() {

    override fun handleSpecificException(
        exception: Exception,
    ): ErrorEntity {
        return when (exception) {
            is SearchContentException -> handleSearchContentException(exception)
            else -> handleUnknownError(exception)
        }
    }

    private fun handleSearchContentException(exception: SearchContentException): ErrorEntity {
        return when (exception) {
            is SearchContentException.SearchContent.InvalidAPIkey -> {
                ErrorEntity.PopularContent.Unauthorized(exception.message)
            }

            is SearchContentException.SearchContent.InvalidPage -> {
                ErrorEntity.PopularContent.InvalidPage(exception.message)
            }

            is SearchContentException.NoConnection -> {
                ErrorEntity.NetworksError.NoInternet(exception.message)
            }

            is SearchContentException.Undefined -> {
                ErrorEntity.UnknownError(exception.message)
            }
        }
    }

}
