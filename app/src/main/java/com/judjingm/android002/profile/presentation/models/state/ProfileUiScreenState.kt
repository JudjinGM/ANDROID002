package com.judjingm.android002.profile.presentation.models.state

import com.judjingm.android002.home.presentation.models.StringVO
import com.judjingm.android002.profile.domain.models.ProfileDetails

sealed interface ProfileUiScreenState {
    data object Loading : ProfileUiScreenState
    data class Initial(val message: StringVO) : ProfileUiScreenState
    data class AuthenticateInProgress(val url: String) : ProfileUiScreenState
    data class Success(val profileDetails: ProfileDetails) : ProfileUiScreenState
}