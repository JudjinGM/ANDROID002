package com.judjingm.android002.home.presentation.ui

import androidx.lifecycle.viewModelScope
import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.ui.BaseViewModel
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.home.domain.models.Movie
import com.judjingm.android002.home.domain.models.TVShow
import com.judjingm.android002.home.domain.useCase.GetPopularMoviesUseCase
import com.judjingm.android002.home.domain.useCase.GetPopularTVShowsUseCase
import com.judjingm.android002.home.presentation.models.StringVO
import com.judjingm.android002.home.presentation.models.state.HomeScreenEvents
import com.judjingm.android002.home.presentation.models.state.HomeScreenSideEffects
import com.judjingm.android002.home.presentation.models.state.HomeScreenState
import com.judjingm.android002.home.presentation.models.state.HomeScreenUiState
import com.judjingm.android002.home.presentation.models.state.UiErrorsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getPopularTVShowsUseCase: GetPopularTVShowsUseCase,
    private val commonUIMapper: CommonUiMapper
) : BaseViewModel() {

    private val _state: MutableStateFlow<HomeScreenState> = MutableStateFlow(HomeScreenState())

    private val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    private val _uiState: MutableStateFlow<HomeScreenUiState> =
        MutableStateFlow(HomeScreenUiState(HomeScreenUiState.UiState.Loading))

    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    private val _sideEffects: MutableSharedFlow<HomeScreenSideEffects> = MutableSharedFlow()

    val sideEffect: SharedFlow<HomeScreenSideEffects> = _sideEffects.asSharedFlow()

    fun handleEvent(event: HomeScreenEvents) {
        when (event) {
            HomeScreenEvents.GetPopularContent -> getPopularContent()
            HomeScreenEvents.InitializeViewModel -> initialize()
        }
    }

    private fun initialize() {
        viewModelScope.launch {
            state.collect { screenState ->
                if (screenState.popularContent.content.isEmpty()) {
                    _uiState.update {
                        HomeScreenUiState(
                            HomeScreenUiState.UiState.Success.Empty
                        )
                    }
                } else {
                    _uiState.update {
                        HomeScreenUiState(
                            HomeScreenUiState.UiState.Success.Content(screenState.popularContent.content)
                        )
                    }
                }

                if (screenState.isErrorFetchingMovie && screenState.isErrorFetchingTVShows) {
                    _uiState.update {
                        HomeScreenUiState(
                            HomeScreenUiState.UiState.Error(UiErrorsState.CouldNotFetchData)
                        )
                    }
                } else if (screenState.isErrorFetchingMovie) {
                    _sideEffects.emit(HomeScreenSideEffects.ShowMessage(screenState.errorMessage))

                } else if (screenState.isErrorFetchingTVShows) {
                    _sideEffects.emit(HomeScreenSideEffects.ShowMessage(screenState.errorMessage))
                }
            }
        }
    }

    private fun getPopularContent() {
        viewModelScope.launch {
            getPopularMoviesUseCase
                .execute(page = state.value.currentPage).zip(
                    getPopularTVShowsUseCase.execute(page = state.value.currentPage)
                ) { popularMovies, popularTVShows ->

                    val popularContentMutex = Mutex()
                    var pagedList = state.value.popularContent

                    popularMovies.handle(object :
                        Resource.ResultHandler<PagedList<Movie>, ErrorEntity> {
                        override suspend fun handleSuccess(data: PagedList<Movie>) {
                            popularContentMutex.withLock {
                                pagedList += commonUIMapper.toPagedList(
                                    data
                                ) { commonUIMapper.toPopularContent(it) }
                            }
                        }

                        override suspend fun handleError(errorStatus: ErrorEntity) {
                            _state.update { screenState ->
                                handleMovieFetchingError(screenState, errorStatus)
                            }
                        }
                    })

                    popularTVShows.handle(
                        object :
                            Resource.ResultHandler<PagedList<TVShow>, ErrorEntity> {
                            override suspend fun handleSuccess(data: PagedList<TVShow>) {
                                popularContentMutex.withLock {
                                    pagedList += commonUIMapper.toPagedList(
                                        data
                                    ) { commonUIMapper.toPopularContent(it) }
                                }
                            }

                            override suspend fun handleError(errorStatus: ErrorEntity) {
                                _state.update { screenState ->
                                    handleMovieTvShowError(screenState, errorStatus)
                                }
                            }
                        })
                    return@zip pagedList
                }.collect { data ->
                    _state.update {
                        it.copy(
                            popularContent = data,
                            currentPage = data.currentPage + 1
                        )
                    }
                }
        }
    }

    fun handleMovieFetchingError(
        state: HomeScreenState,
        errorStatus: ErrorEntity
    ): HomeScreenState {
        return when (errorStatus) {
            is ErrorEntity.NetworksError.NoInternet -> {
                state.copy(
                    isErrorFetchingMovie = true,
                    errorMessage = StringVO.Plain(errorStatus.message)
                )
            }

            is ErrorEntity.PopularContent.InvalidPage -> {
                state.copy(
                    isErrorFetchingMovie = true,
                    errorMessage = StringVO.Plain(errorStatus.message)
                )
            }

            is ErrorEntity.PopularContent.Unauthorized -> {
                state.copy(
                    isErrorFetchingMovie = true,
                    errorMessage = StringVO.Plain(errorStatus.message)
                )
            }

            is ErrorEntity.UnknownError -> {
                state.copy(
                    isErrorFetchingMovie = true,
                    errorMessage = StringVO.Plain(errorStatus.message)
                )
            }
        }
    }

    fun handleMovieTvShowError(
        state: HomeScreenState,
        errorStatus: ErrorEntity
    ): HomeScreenState {
        return when (errorStatus) {
            is ErrorEntity.NetworksError.NoInternet -> {
                state.copy(
                    isErrorFetchingMovie = true,
                    errorMessage = StringVO.Plain(errorStatus.message)
                )
            }

            is ErrorEntity.PopularContent.InvalidPage -> {
                state.copy(
                    isErrorFetchingMovie = true,
                    errorMessage = StringVO.Plain(errorStatus.message)
                )
            }

            is ErrorEntity.PopularContent.Unauthorized -> {
                state.copy(
                    isErrorFetchingMovie = true,
                    errorMessage = StringVO.Plain(errorStatus.message)
                )
            }

            is ErrorEntity.UnknownError -> {
                state.copy(
                    isErrorFetchingMovie = true,
                    errorMessage = StringVO.Plain(errorStatus.message)
                )
            }
        }
    }
}