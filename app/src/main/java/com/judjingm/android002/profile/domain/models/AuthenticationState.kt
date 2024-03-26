package com.judjingm.android002.profile.domain.models

data class AuthenticationState(
    val state: State = State.Initial
) {
    sealed interface State {
        data object Initial : State
        data class InProcess(val requestToken: String) : State
    }
}