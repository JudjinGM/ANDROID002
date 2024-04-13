package com.judjingm.android002.upload.presentation.chooseDocument

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.judjingm.android002.R
import com.judjingm.android002.common.ui.BaseViewModel
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.home.presentation.models.StringVO
import com.judjingm.android002.upload.domain.models.FileResult
import com.judjingm.android002.upload.domain.useCase.SavePdfToPrivateStorageUseCase
import com.judjingm.android002.upload.presentation.models.state.ChooseDocumentErrorState
import com.judjingm.android002.upload.presentation.models.state.ChooseDocumentEvent
import com.judjingm.android002.upload.presentation.models.state.ChooseDocumentScreenState
import com.judjingm.android002.upload.presentation.models.state.ChooseDocumentSideEffects
import com.judjingm.android002.upload.presentation.models.state.ChooseDocumentUiScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseDocumentViewModel @Inject constructor(
    private val savePdfToPrivateStorageUseCase: SavePdfToPrivateStorageUseCase,
) : BaseViewModel() {

    private val _state: MutableStateFlow<ChooseDocumentScreenState> =
        MutableStateFlow(ChooseDocumentScreenState())

    val state: StateFlow<ChooseDocumentScreenState> = _state.asStateFlow()

    private val _uiState: MutableStateFlow<ChooseDocumentUiScreenState> =
        MutableStateFlow(ChooseDocumentUiScreenState.Initial)

    val uiState: StateFlow<ChooseDocumentUiScreenState> = _uiState.asStateFlow()

    private val _sideEffects: MutableSharedFlow<ChooseDocumentSideEffects> = MutableSharedFlow()

    val sideEffect: SharedFlow<ChooseDocumentSideEffects> = _sideEffects.asSharedFlow()

    init {
        viewModelScope.launch {
            state.collect { screenState ->
                when (screenState.errorState) {

                    is ChooseDocumentErrorState.CannotChooseDocumentFile -> {
                        ChooseDocumentSideEffects
                            .ShowMessage(StringVO.Plain(screenState.errorState.error))
                    }


                    ChooseDocumentErrorState.UnknownError -> {
                        _sideEffects.emit(
                            ChooseDocumentSideEffects
                                .ShowMessage(StringVO.Resource(R.string.error_something_went_wrong))
                        )
                    }

                    ChooseDocumentErrorState.NoError -> with(screenState) {
                        if (isLoading) {
                            _uiState.update {
                                ChooseDocumentUiScreenState.Loading
                            }
                        } else if (documentUri.toString().isNotBlank()) {
                            _uiState.update {
                                ChooseDocumentUiScreenState.Success(
                                    name = documentName,
                                    uri = documentUri
                                )
                            }
                        } else {
                            _uiState.update {
                                ChooseDocumentUiScreenState.Initial
                            }
                        }
                    }
                }
            }
        }

    }

    fun handleEvent(event: ChooseDocumentEvent) {
        when (event) {
            ChooseDocumentEvent.ChooseDocumentClicked -> {
                _state.update {
                    it.copy(
                        isLoading = true
                    )
                }
                viewModelScope.launch {
                    _sideEffects.emit(ChooseDocumentSideEffects.OpenDocumentPicker)
                }
            }

            is ChooseDocumentEvent.DocumentSelected -> {
                saveDocument(event.uri, event.name)
            }

            ChooseDocumentEvent.ProceedNextClicked -> {
                viewModelScope.launch {
                    _sideEffects.emit(ChooseDocumentSideEffects.NavigateToNextScreen)
                }
            }

            ChooseDocumentEvent.BackClicked -> {
                viewModelScope.launch {
                    _sideEffects.emit(ChooseDocumentSideEffects.NavigateToPreviousScreen)
                }
            }
        }
    }

    private fun saveDocument(uri: Uri, name: String) {
        viewModelScope.launch {
            savePdfToPrivateStorageUseCase.invoke(uri, name).handle(
                object : Resource.ResultHandler<FileResult, String> {
                    override suspend fun handleSuccess(data: FileResult) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                documentUri = data.uri,
                                documentName = data.originalName
                            )
                        }
                    }

                    override suspend fun handleError(error: String) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorState = ChooseDocumentErrorState.CannotChooseDocumentFile(error),
                            )
                        }
                    }

                })
        }
    }

}