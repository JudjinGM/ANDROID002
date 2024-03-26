package com.judjingm.android002.profile.presentation.models.state

import com.judjingm.android002.profile.domain.models.ProfileDetails

data class ProfileScreenState(
    val profileDetails: ProfileDetails = ProfileDetails(),
    val url: String = "",
    val isLoading: Boolean = false,
    val isAuthenticationInProgress: Boolean = false,
    val isAuthenticationIsSuccess: Boolean = false,
    val errorState: AuthenticationErrorState = AuthenticationErrorState.NoError
)
