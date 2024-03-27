package com.judjingm.android002.details.presentation.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.cashadvisor.common.utill.extensions.logDebugMessage
import com.judjingm.android002.R
import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.ui.BaseViewModel
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.details.domain.useCase.GetLanguageForAppUseCase
import com.judjingm.android002.details.domain.useCase.GetMovieCreditsUseCase
import com.judjingm.android002.details.domain.useCase.GetMoviesDetailsUseCase
import com.judjingm.android002.details.domain.useCase.GetTvShowCreditsUseCase
import com.judjingm.android002.details.domain.useCase.GetTvShowDetailsUseCase
import com.judjingm.android002.details.presentation.models.ContentDetailErrorState
import com.judjingm.android002.details.presentation.models.ContentDetailEvent
import com.judjingm.android002.details.presentation.models.ContentDetailSideEffect
import com.judjingm.android002.details.presentation.models.ContentDetailUiScreenState
import com.judjingm.android002.details.presentation.models.ContentDetailsUi
import com.judjingm.android002.home.presentation.models.ContentType
import com.judjingm.android002.home.presentation.models.StringVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentDetailViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val getMoviesDetailsUseCase: GetMoviesDetailsUseCase,
    private val getTvShowDetailsUseCase: GetTvShowDetailsUseCase,
    private val getMovieCreditsUseCase: GetMovieCreditsUseCase,
    private val getTvShowCreditsUseCase: GetTvShowCreditsUseCase,
    private val getLanguageForAppUseCase: GetLanguageForAppUseCase,
    private val contentDetailsDomainToUiMapper: ContentDetailsDomainToUiMapper,
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<ContentDetailUiScreenState> =
        MutableStateFlow(ContentDetailUiScreenState.Loading)

    val uiState: StateFlow<ContentDetailUiScreenState> = _uiState.asStateFlow()

    private val _sideEffects: MutableSharedFlow<ContentDetailSideEffect> = MutableSharedFlow()

    val sideEffect: SharedFlow<ContentDetailSideEffect> = _sideEffects.asSharedFlow()

    private val args = ContentDetailFragmentArgs.fromSavedStateHandle(savedState)

    private val contentType = args.contentType
    private val contentId = args.contentId


    init {
        getContent()
    }

    fun handleEvent(event: ContentDetailEvent) {
        when (event) {
            is ContentDetailEvent.OnBackClicked -> {
                viewModelScope.launch {
                    _sideEffects.emit(ContentDetailSideEffect.NavigateBack)
                }
            }
        }
    }

    private fun getContent() {
        when (contentType) {
            ContentType.MOVIE -> getMovieDetails()
            ContentType.TVSHOW -> getTvShowDetails()
            ContentType.UNKNOWN -> {
                _uiState.value =
                    ContentDetailUiScreenState.Error(
                        errorState = ContentDetailErrorState.UnknownError(
                            StringVO.Resource(R.string.error_something_went_wrong)
                        )
                    )
            }
        }
    }

    private fun getMovieDetails() {
        viewModelScope.launch {
            logDebugMessage("system language = ${getLanguageForAppUseCase()}")
            getMoviesDetailsUseCase(contentId, getLanguageForAppUseCase())
                .onStart {
                    _uiState.value = ContentDetailUiScreenState.Loading
                }
                .zip(
                    getMovieCreditsUseCase(
                        contentId,
                        getLanguageForAppUseCase()
                    )
                ) { movie, credits ->
                    return@zip when (movie) {
                        is Resource.Success ->
                            when (credits) {
                                is Resource.Success -> {
                                    Resource.Success<ContentDetailsUi, ErrorEntity>(
                                        contentDetailsDomainToUiMapper.toContentDetailsUi(
                                            movie.data, credits.data
                                        )
                                    )
                                }

                                is Resource.Error -> Resource.Error<ContentDetailsUi, ErrorEntity>(
                                    credits.error
                                )

                            }

                        is Resource.Error -> Resource.Error<ContentDetailsUi, ErrorEntity>(
                            movie.error
                        )
                    }
                }
                .collect { resource ->
                    resource.handle(object : Resource.ResultHandler<ContentDetailsUi, ErrorEntity> {
                        override suspend fun handleSuccess(data: ContentDetailsUi) {
                            _uiState.update {
                                ContentDetailUiScreenState.Success(data)
                            }
                        }

                        override suspend fun handleError(errorStatus: ErrorEntity) {
                            val errorState: ContentDetailErrorState = when (errorStatus) {
                                is ErrorEntity.NetworksError.NoInternet -> ContentDetailErrorState.NoConnection(
                                    StringVO.Resource(R.string.error_no_connection)
                                )

                                is ErrorEntity.ContentDetail.Unauthorized,
                                is ErrorEntity.NetworksError.NoConnection -> ContentDetailErrorState.NoConnection(
                                    StringVO.Resource(R.string.error_service_problem)
                                )

                                else -> ContentDetailErrorState.UnknownError(
                                    StringVO.Resource(R.string.error_something_went_wrong)
                                )

                            }
                            _uiState.update {
                                ContentDetailUiScreenState.Error(errorState)
                            }
                        }

                    })
                }
        }
    }

    private fun getTvShowDetails() {
        logDebugMessage("system language = ${getLanguageForAppUseCase()}")
        viewModelScope.launch {
            getTvShowDetailsUseCase(contentId, getLanguageForAppUseCase())
                .onStart {
                    _uiState.value = ContentDetailUiScreenState.Loading
                }
                .zip(
                    getTvShowCreditsUseCase(
                        contentId,
                        getLanguageForAppUseCase()
                    )
                ) { tvShow, credits ->
                    return@zip when (tvShow) {
                        is Resource.Success ->
                            when (credits) {
                                is Resource.Success -> {
                                    Resource.Success<ContentDetailsUi, ErrorEntity>(
                                        contentDetailsDomainToUiMapper.toContentDetailsUi(
                                            tvShow.data, credits.data
                                        )
                                    )
                                }

                                is Resource.Error -> Resource.Error<ContentDetailsUi, ErrorEntity>(
                                    credits.error
                                )

                            }

                        is Resource.Error -> Resource.Error<ContentDetailsUi, ErrorEntity>(
                            tvShow.error
                        )
                    }
                }
                .collect { resource ->
                    resource.handle(object : Resource.ResultHandler<ContentDetailsUi, ErrorEntity> {
                        override suspend fun handleSuccess(data: ContentDetailsUi) {
                            _uiState.update {
                                ContentDetailUiScreenState.Success(data)
                            }
                        }

                        override suspend fun handleError(errorStatus: ErrorEntity) {
                            val errorState: ContentDetailErrorState = when (errorStatus) {
                                is ErrorEntity.NetworksError.NoInternet -> ContentDetailErrorState.NoConnection(
                                    StringVO.Resource(R.string.error_no_connection)
                                )

                                is ErrorEntity.ContentDetail.Unauthorized -> ContentDetailErrorState.NoConnection(
                                    StringVO.Resource(R.string.error_service_problem)
                                )

                                else -> ContentDetailErrorState.UnknownError(
                                    StringVO.Resource(R.string.error_something_went_wrong)
                                )

                            }
                            _uiState.update {
                                ContentDetailUiScreenState.Error(errorState)
                            }
                        }

                    })
                }
        }
    }

}