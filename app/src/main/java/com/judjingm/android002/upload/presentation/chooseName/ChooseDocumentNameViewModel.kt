package com.judjingm.android002.upload.presentation.chooseName

import androidx.lifecycle.viewModelScope
import com.judjingm.android002.common.ui.BaseViewModel
import com.judjingm.android002.upload.domain.useCase.GetFileNameUseCase
import com.judjingm.android002.upload.domain.useCase.SetFileNameUseCase
import com.judjingm.android002.upload.presentation.models.chooseDocument.ChooseDocumentNameEvent
import com.judjingm.android002.upload.presentation.models.chooseDocument.ChooseDocumentNameSideEffects
import com.judjingm.android002.upload.presentation.models.chooseDocument.ChooseDocumentNameUiScreenState
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
class ChooseDocumentNameViewModel @Inject constructor(
    private val getFileNameUseCase: GetFileNameUseCase,
    private val setFileNameUseCase: SetFileNameUseCase,
) : BaseViewModel() {
    private val _state: MutableStateFlow<String> = MutableStateFlow(BLANC_NAME)

    private val state: StateFlow<String> = _state.asStateFlow()

    private
    val _uiState: MutableStateFlow<ChooseDocumentNameUiScreenState> =
        MutableStateFlow(ChooseDocumentNameUiScreenState.Loading)

    val uiState: StateFlow<ChooseDocumentNameUiScreenState> = _uiState.asStateFlow()

    private val _sideEffects: MutableSharedFlow<ChooseDocumentNameSideEffects> = MutableSharedFlow()

    val sideEffect: SharedFlow<ChooseDocumentNameSideEffects> = _sideEffects.asSharedFlow()

    init {
        viewModelScope.launch {
            getFileNameUseCase.invoke().onStart {
                _uiState.update {
                    ChooseDocumentNameUiScreenState.Loading
                }
            }.collect { name ->
                _state.update {
                    name
                }
            }
        }

        viewModelScope.launch {
            state.collect { stateName ->
                _uiState.update {
                    ChooseDocumentNameUiScreenState.Success(stateName)
                }
            }
        }
    }

    fun handleEvent(event: ChooseDocumentNameEvent) {
        when (event) {
            is ChooseDocumentNameEvent.OnDocumentNameChanged -> {
                _state.update {
                    event.name
                }
            }

            ChooseDocumentNameEvent.ProceedNextClicked -> viewModelScope.launch {
                setFileNameUseCase.invoke(state.value)
                _sideEffects.emit(ChooseDocumentNameSideEffects.NavigateToNextScreen)
            }
        }
    }

    companion object {
        const val BLANC_NAME = ""
    }

}