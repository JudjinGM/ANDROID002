package com.judjingm.android002.upload.presentation.confirmUpload

import androidx.lifecycle.viewModelScope
import com.judjingm.android002.R
import com.judjingm.android002.common.ui.BaseViewModel
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.home.presentation.models.StringVO
import com.judjingm.android002.upload.domain.useCase.GetFileStateUseCase
import com.judjingm.android002.upload.domain.useCase.UploadPdfToServerUseCase
import com.judjingm.android002.upload.presentation.models.state.ConfirmUploadErrorState
import com.judjingm.android002.upload.presentation.models.state.ConfirmUploadEvent
import com.judjingm.android002.upload.presentation.models.state.ConfirmUploadScreenState
import com.judjingm.android002.upload.presentation.models.state.ConfirmUploadSideEffects
import com.judjingm.android002.upload.presentation.models.state.ConfirmUploadUiErrorState
import com.judjingm.android002.upload.presentation.models.state.ConfirmUploadUiScreenState
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
class ConfirmUploadViewModel @Inject constructor(
    private val getFileStateUseCase: GetFileStateUseCase,
    private val uploadPdfToServerUseCase: UploadPdfToServerUseCase,

    ) : BaseViewModel() {
    private val _state: MutableStateFlow<ConfirmUploadScreenState> =
        MutableStateFlow(ConfirmUploadScreenState())

    val state: StateFlow<ConfirmUploadScreenState> = _state.asStateFlow()

    private val _uiState: MutableStateFlow<ConfirmUploadUiScreenState> =
        MutableStateFlow(ConfirmUploadUiScreenState.Loading)

    val uiState: StateFlow<ConfirmUploadUiScreenState> = _uiState.asStateFlow()

    private val _sideEffects: MutableSharedFlow<ConfirmUploadSideEffects> = MutableSharedFlow()

    val sideEffect: SharedFlow<ConfirmUploadSideEffects> = _sideEffects.asSharedFlow()

    init {
        viewModelScope.launch {
            val fileState = getFileStateUseCase()
            _state.update {
                it.copy(
                    documentName = fileState.fileName,
                    documentUri = fileState.localFileUri
                )
            }
        }

        viewModelScope.launch {
            state.collect { screenState ->
                when (screenState.errorState) {
                    is ConfirmUploadErrorState.NoInternet -> {
                        _sideEffects.emit(
                            ConfirmUploadSideEffects
                                .ShowMessage(StringVO.Resource(R.string.error_no_internet))
                        )
                    }

                    ConfirmUploadErrorState.NoConnection -> {
                        _sideEffects.emit(
                            ConfirmUploadSideEffects
                                .ShowMessage(StringVO.Resource(R.string.error_service_problem))
                        )
                    }

                    is ConfirmUploadErrorState.CannotUploadFile -> {
                        _uiState.update {
                            ConfirmUploadUiScreenState.Error(
                                ConfirmUploadUiErrorState.FileUploadError(
                                    StringVO.Resource(R.string.error_something_went_wrong)
                                )
                            )
                        }
                    }

                    ConfirmUploadErrorState.UnknownError -> {
                        _sideEffects.emit(
                            ConfirmUploadSideEffects
                                .ShowMessage(StringVO.Resource(R.string.error_something_went_wrong))
                        )
                    }

                    ConfirmUploadErrorState.CannotShowFile -> {
                        _uiState.update {
                            ConfirmUploadUiScreenState.Error(
                                ConfirmUploadUiErrorState.FileLoadError(
                                    StringVO.Resource(R.string.error_something_went_wrong)
                                )
                            )
                        }
                    }

                    ConfirmUploadErrorState.NoError -> with(screenState) {
                        if (isUploading) {
                            _uiState.update {
                                ConfirmUploadUiScreenState.Uploading
                            }
                        } else if (isUploadComplete) {
                            _uiState.update {
                                ConfirmUploadUiScreenState.Success
                            }
                        } else {
                            _uiState.update {
                                ConfirmUploadUiScreenState.ReadyToUplLoad(
                                    name = documentName,
                                    uri = documentUri
                                )
                            }
                        }

                    }
                }
            }
        }
    }

    fun handleEvent(event: ConfirmUploadEvent) {
        when (event) {
            ConfirmUploadEvent.ConfirmButtonClicked -> {
                uploadDocument()
            }

            ConfirmUploadEvent.BackButtonClicked -> {
                viewModelScope.launch {
                    _sideEffects.emit(ConfirmUploadSideEffects.NavigateBack)
                }
            }

            ConfirmUploadEvent.CancelButtonClicked -> {
                viewModelScope.launch {
                    _sideEffects.emit(ConfirmUploadSideEffects.NavigateToHome)
                }
            }

            ConfirmUploadEvent.ErrorShowingPDf -> {
                _state.update {
                    it.copy(
                        errorState = ConfirmUploadErrorState.CannotShowFile
                    )
                }
            }

            ConfirmUploadEvent.RetryButtonClicked -> {
                uploadDocument()
            }
        }
    }

    private fun uploadDocument() {
        viewModelScope.launch {
            uploadPdfToServerUseCase()
                .onStart {
                    _state.update {
                        it.copy(
                            errorState = ConfirmUploadErrorState.NoError
                        )
                    }
                }
                .collect { resource ->
                    resource.handle(object : Resource.ResultHandler<Boolean, String> {
                        override suspend fun handleSuccess(data: Boolean) {
                            if (data) {
                                _state.update {
                                    it.copy(
                                        isUploadComplete = true,
                                        isUploading = false
                                    )
                                }
                            } else {
                                _state.update {
                                    it.copy(
                                        isUploading = true
                                    )
                                }
                            }
                        }

                        override suspend fun handleError(error: String) {
                            _state.update {
                                it.copy(
                                    isUploading = false,
                                    isUploadComplete = false,
                                    errorState = ConfirmUploadErrorState.CannotUploadFile(error),
                                )
                            }
                        }
                    })
                }
        }
    }

}