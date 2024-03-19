package com.judjingm.android002.home.presentation.models.state

sealed interface HomeScreenEvents {
    data object InitializeViewModel : HomeScreenEvents
    data object GetPopularContent : HomeScreenEvents
    data object OnContentClicked : HomeScreenEvents

}