package com.judjingm.android002.upload.presentation.choseDocument

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.judjingm.android002.R
import com.judjingm.android002.common.ui.BaseViewModel
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.home.presentation.models.StringVO
import com.judjingm.android002.upload.domain.useCase.SavePdfToPrivateStorageUseCase
import com.judjingm.android002.upload.presentation.models.chooseDocument.ChooseDocumentErrorState
import com.judjingm.android002.upload.presentation.models.chooseDocument.ChooseDocumentEvent
import com.judjingm.android002.upload.presentation.models.chooseDocument.ChooseDocumentScreenState
import com.judjingm.android002.upload.presentation.models.chooseDocument.ChooseDocumentSideEffects
import com.judjingm.android002.upload.presentation.models.chooseDocument.ChooseDocumentUiScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
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
                    is ChooseDocumentErrorState.NoInternet -> {
                        _sideEffects.emit(
                            ChooseDocumentSideEffects
                                .ShowMessage(StringVO.Resource(R.string.error_no_internet))
                        )
                    }

                    ChooseDocumentErrorState.NoConnection -> {
                        _sideEffects.emit(
                            ChooseDocumentSideEffects
                                .ShowMessage(StringVO.Resource(R.string.error_service_problem))
                        )
                    }

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
                                ChooseDocumentUiScreenState.Success(documentUri)
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
                saveDocument(event.uri)
            }

            ChooseDocumentEvent.ProceedNextClicked -> {
                viewModelScope.launch {
                    _sideEffects.emit(ChooseDocumentSideEffects.NavigateToNextScreen)
                }
            }
        }

    }

    private fun saveDocument(uri: Uri) {
        viewModelScope.launch {
            savePdfToPrivateStorageUseCase(uri).handle(
                object : Resource.ResultHandler<File, String> {
                    override suspend fun handleSuccess(data: File) {
                        val savedFileUri = Uri.fromFile(data)
                        _state.update {
                            it.copy(
                                isLoading = false,
                                documentUri = savedFileUri
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

//    private fun uploadDocument(uri: Uri) {
//        viewModelScope.launch {
//            uploadDocumentUseCase(uri = uri).handle(object :
//                Resource.ResultHandler<Boolean, String> {
//                override suspend fun handleSuccess(data: Boolean) {
//                    _state.update {
//                        it.copy(
//                            isLoading = false,
//                            isUploadSuccess = true
//                        )
//                    }
//                }
//
//                override suspend fun handleError(error: String) {
//                    _state.update {
//                        it.copy(
//                            isLoading = false,
//                            isUploadSuccess = false,
//                            errorState = ChooseDocumentErrorState.CannotChooseDocumentFile(error = error)
//                        )
//                    }
//
//                }
//            })
//        }
//    }
}