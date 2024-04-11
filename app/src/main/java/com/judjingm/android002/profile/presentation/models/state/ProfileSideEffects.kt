package com.judjingm.android002.profile.presentation.models.state

import com.judjingm.android002.home.presentation.models.StringVO

sealed interface ProfileSideEffects {

    data class ShowMessage(val message: StringVO) : ProfileSideEffects
    data object NavigateToUploadPdf : ProfileSideEffects

}