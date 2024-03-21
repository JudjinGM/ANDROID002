package com.judjingm.android002.home.presentation.models.state

import com.judjingm.android002.home.presentation.models.PopularContentUi

sealed interface HomeScreenEvent {
    data object PaginationTriggered : HomeScreenEvent
    data object RefreshButtonClicked : HomeScreenEvent
    data class OnContentClicked(val content: PopularContentUi) : HomeScreenEvent

}