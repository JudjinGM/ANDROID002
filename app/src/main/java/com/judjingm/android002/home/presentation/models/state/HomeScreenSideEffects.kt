package com.judjingm.android002.home.presentation.models.state

import com.judjingm.android002.home.presentation.models.ContentType
import com.judjingm.android002.home.presentation.models.StringVO

sealed interface HomeScreenSideEffects {
    data class ShowMessage(val message: StringVO) : HomeScreenSideEffects
    data class NavigateToDetails(val id: Int, val contentType: ContentType) : HomeScreenSideEffects
}