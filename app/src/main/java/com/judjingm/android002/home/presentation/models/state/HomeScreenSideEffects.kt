package com.judjingm.android002.home.presentation.models.state

import com.judjingm.android002.home.presentation.models.StringVO

sealed interface HomeScreenSideEffects {
    class ShowMessage(val message: StringVO) : HomeScreenSideEffects
}