package com.judjingm.android002.search.presentation.models.state

import com.judjingm.android002.home.presentation.models.ContentType
import com.judjingm.android002.home.presentation.models.StringVO

sealed interface SearchScreenSideEffects {
    data class ShowMessage(val message: StringVO) : SearchScreenSideEffects
    data class NavigateToDetails(val id: Int, val contentType: ContentType) :
        SearchScreenSideEffects
}