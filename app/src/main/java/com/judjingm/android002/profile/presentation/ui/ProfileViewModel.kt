package com.judjingm.android002.profile.presentation.ui

import androidx.lifecycle.viewModelScope
import app.cashadvisor.common.utill.extensions.logDebugMessage
import com.judjingm.android002.R
import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.ui.BaseViewModel
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.home.presentation.models.StringVO
import com.judjingm.android002.profile.domain.models.Authentication
import com.judjingm.android002.profile.domain.models.LoginData
import com.judjingm.android002.profile.domain.models.ProfileDetails
import com.judjingm.android002.profile.domain.useCase.AuthenticationUseCase
import com.judjingm.android002.profile.domain.useCase.ConfirmLoginToProfileUseCase
import com.judjingm.android002.profile.domain.useCase.DeleteInvalidCredentialsUseCase
import com.judjingm.android002.profile.domain.useCase.GetProfileDetailsUseCase
import com.judjingm.android002.profile.domain.useCase.LogoutFromProfileUseCase
import com.judjingm.android002.profile.domain.useCase.RequestLoginToProfileUseCase
import com.judjingm.android002.profile.presentation.models.state.AuthenticationErrorState
import com.judjingm.android002.profile.presentation.models.state.ProfileScreenState
import com.judjingm.android002.profile.presentation.models.state.ProfileSideEffects
import com.judjingm.android002.profile.presentation.models.state.ProfileUiScreenState
import com.judjingm.android002.search.presentation.models.state.ProfileEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationUseCase: AuthenticationUseCase,
    private val requestLoginToProfileUseCase: RequestLoginToProfileUseCase,
    private val confirmLoginToProfileUseCase: ConfirmLoginToProfileUseCase,
    private val logoutFromProfileUseCase: LogoutFromProfileUseCase,
    private val getProfileUseCase: GetProfileDetailsUseCase,
    private val deleteInvalidCredentialsUseCase: DeleteInvalidCredentialsUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<ProfileScreenState> =
        MutableStateFlow(ProfileScreenState())

    val state: StateFlow<ProfileScreenState> = _state.asStateFlow()

    private val _uiState: MutableStateFlow<ProfileUiScreenState> =
        MutableStateFlow(ProfileUiScreenState.Loading)

    val uiState: StateFlow<ProfileUiScreenState> = _uiState.asStateFlow()

    private val _sideEffects: MutableSharedFlow<ProfileSideEffects> = MutableSharedFlow()

    val sideEffect: SharedFlow<ProfileSideEffects> = _sideEffects.asSharedFlow()

    init {
        initialize()
        viewModelScope.launch {
            state.collect { screenState ->
                when (screenState.errorState) {
                    AuthenticationErrorState.NoInternet -> {
                        viewModelScope.launch {
                            _sideEffects.emit(
                                ProfileSideEffects
                                    .ShowMessage(StringVO.Resource(R.string.error_no_connection))
                            )
                        }
                        _state.update {
                            it.copy(
                                errorState = AuthenticationErrorState.NoError
                            )
                        }
                    }

                    AuthenticationErrorState.CannotProceedTryAgain -> {
                        viewModelScope.launch {
                            _sideEffects.emit(
                                ProfileSideEffects
                                    .ShowMessage(StringVO.Resource(R.string.error_try_again))
                            )
                            if (screenState.isAuthenticationInProgress) {
                                _state.update {
                                    it.copy(
                                        isAuthenticationIsSuccess = false,
                                        errorState = AuthenticationErrorState.NoError
                                    )
                                }
                            }
                        }
                    }

                    AuthenticationErrorState.UnknownError -> {
                        viewModelScope.launch {
                            _sideEffects.emit(
                                ProfileSideEffects
                                    .ShowMessage(StringVO.Resource(R.string.error_something_went_wrong))
                            )
                        }
                        if (screenState.isAuthenticationInProgress) {
                            _state.update {
                                it.copy(
                                    isAuthenticationInProgress = false,
                                    isAuthenticationIsSuccess = false,
                                    errorState = AuthenticationErrorState.NoError
                                )
                            }
                        } else {
                            _state.update {
                                it.copy(
                                    errorState = AuthenticationErrorState.NoError
                                )
                            }
                        }
                    }

                    AuthenticationErrorState.InvalidCredentials -> {
                        deleteInvalidCredentialsUseCase()
                        viewModelScope.launch {
                            _sideEffects.emit(
                                ProfileSideEffects
                                    .ShowMessage(StringVO.Resource(R.string.session_done))
                            )
                        }
                    }

                    AuthenticationErrorState.LoginDeniedByUser -> {
                        _sideEffects.emit(
                            ProfileSideEffects
                                .ShowMessage(StringVO.Resource(R.string.login_denied_by_user))
                        )
                        _state.update {
                            it.copy(
                                errorState = AuthenticationErrorState.NoError
                            )
                        }
                    }

                    AuthenticationErrorState.NoError -> {
                        if (screenState.isLoading) {
                            _uiState.update {
                                ProfileUiScreenState.Loading
                            }
                        } else if (screenState.isAuthenticationIsSuccess) {
                            _uiState.update {
                                ProfileUiScreenState.Success(screenState.profileDetails)
                            }
                        } else if (screenState.isAuthenticationInProgress) {
                            _uiState.update {
                                ProfileUiScreenState.AuthenticateInProgress(screenState.url)
                            }
                        } else _uiState.update {
                            ProfileUiScreenState.Initial(StringVO.Resource(R.string.enter_to_profile))
                        }
                    }

                }
            }
        }
    }

    fun handleEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.OnLoginTaped -> {
                viewModelScope.launch {
                    requestLoginToProfile()
                }
            }

            ProfileEvent.OnLogoutTaped -> {
                viewModelScope.launch {
                    logoutFromProfile()
                }
            }

            ProfileEvent.RequestTokenConfirmed -> {
                viewModelScope.launch {
                    confirmLoginToProfile()
                }
            }

            ProfileEvent.RequestTokenDenied -> {
                requestLoginDenied()
            }
        }
    }

    private fun initialize() {
        viewModelScope.launch {
            authenticationUseCase().onStart {
                _state.update {
                    it.copy(
                        isLoading = true
                    )
                }
            }.collect { authentication ->
                if (authentication is Authentication.Authenticated) {
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                    getProfileUseCase().handle(object :
                        Resource.ResultHandler<ProfileDetails, ErrorEntity> {
                        override suspend fun handleSuccess(data: ProfileDetails) {
                            logDebugMessage("getProfileUseCase handleSuccess $data")
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    isAuthenticationIsSuccess = true,
                                    profileDetails = data
                                )
                            }
                        }

                        override suspend fun handleError(errorStatus: ErrorEntity) {
                            val error = when (errorStatus) {
                                is ErrorEntity.Authentication.InvalidCredentials -> AuthenticationErrorState.InvalidCredentials
                                is ErrorEntity.Authentication.InvalidToken -> AuthenticationErrorState.InvalidCredentials
                                is ErrorEntity.NetworksError.NoInternet -> AuthenticationErrorState.NoInternet
                                else -> AuthenticationErrorState.UnknownError
                            }

                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    errorState = error
                                )
                            }
                        }
                    })

                } else {
                    logDebugMessage("authentication is Authentication.NotAuthenticated")
                    _state.update {
                        ProfileScreenState()
                    }
                }
            }
        }
    }

    private fun requestLoginToProfile() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            requestLoginToProfileUseCase().handle(object :
                Resource.ResultHandler<LoginData, ErrorEntity> {

                override suspend fun handleSuccess(data: LoginData) {
                    if (data.success) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                url = data.urlToProceed,
                                isAuthenticationInProgress = true,
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorState = AuthenticationErrorState.CannotProceedTryAgain
                            )
                        }
                    }
                }

                override suspend fun handleError(errorStatus: ErrorEntity) {
                    val error = when (errorStatus) {
                        is ErrorEntity.Authentication.InvalidCredentials -> AuthenticationErrorState.InvalidCredentials
                        is ErrorEntity.Authentication.InvalidToken -> AuthenticationErrorState.CannotProceedTryAgain
                        is ErrorEntity.Authentication.Unauthorized -> AuthenticationErrorState.UnknownError
                        is ErrorEntity.NetworksError.NoInternet -> AuthenticationErrorState.NoInternet
                        else -> AuthenticationErrorState.UnknownError

                    }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorState = error
                        )
                    }
                }
            })
        }
    }

    private fun confirmLoginToProfile() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            confirmLoginToProfileUseCase().handle(object :
                Resource.ResultHandler<Boolean, ErrorEntity> {
                override suspend fun handleSuccess(data: Boolean) {
                    _sideEffects.emit(
                        ProfileSideEffects
                            .ShowMessage(StringVO.Resource(R.string.login_success))
                    )
                    _state.update {
                        it.copy(
                            isAuthenticationInProgress = false
                        )
                    }
                }

                override suspend fun handleError(errorStatus: ErrorEntity) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorState = AuthenticationErrorState.CannotProceedTryAgain
                        )
                    }
                }
            })
        }
    }

    private fun requestLoginDenied() {
        _state.update {
            it.copy(
                isAuthenticationInProgress = false,
                isAuthenticationIsSuccess = false,
                errorState = AuthenticationErrorState.LoginDeniedByUser
            )
        }
    }

    private fun logoutFromProfile() {
        viewModelScope.launch {
            logoutFromProfileUseCase().handle(object :
                Resource.ResultHandler<Boolean, ErrorEntity> {
                override suspend fun handleSuccess(data: Boolean) {
                    _sideEffects.emit(
                        ProfileSideEffects
                            .ShowMessage(StringVO.Resource(R.string.logout_succes))
                    )
                    _state.update {
                        it.copy(
                            isAuthenticationIsSuccess = false,
                        )
                    }
                }

                override suspend fun handleError(errorStatus: ErrorEntity) {
                    _state.update {
                        it.copy(
                            errorState = AuthenticationErrorState.CannotProceedTryAgain,
                        )
                    }
                }
            })
        }
    }

}
