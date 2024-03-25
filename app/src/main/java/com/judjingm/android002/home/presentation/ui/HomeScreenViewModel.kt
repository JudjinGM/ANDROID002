package com.judjingm.android002.home.presentation.ui

import androidx.lifecycle.viewModelScope
import com.judjingm.android002.R
import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.domain.models.Content
import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.ui.BaseViewModel
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.details.domain.useCase.GetLanguageForAppUseCase
import com.judjingm.android002.home.domain.useCase.GetPopularMoviesUseCase
import com.judjingm.android002.home.domain.useCase.GetPopularTVShowsUseCase
import com.judjingm.android002.home.presentation.models.PopularContentUi
import com.judjingm.android002.home.presentation.models.StringVO
import com.judjingm.android002.home.presentation.models.state.HomeScreenEvent
import com.judjingm.android002.home.presentation.models.state.HomeScreenSideEffects
import com.judjingm.android002.home.presentation.models.state.HomeScreenState
import com.judjingm.android002.home.presentation.models.state.HomeScreenUiState
import com.judjingm.android002.home.presentation.models.state.PopularsErrorState
import com.judjingm.android002.home.presentation.models.state.PopularsErrorUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getPopularTVShowsUseCase: GetPopularTVShowsUseCase,
    private val getLanguageForAppUseCase: GetLanguageForAppUseCase,
    private val contentDomainToUIMapper: ContentDomainToUiMapper
) : BaseViewModel() {

    private val _state: MutableStateFlow<HomeScreenState> = MutableStateFlow(HomeScreenState())

    private val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    private val _uiState: MutableStateFlow<HomeScreenUiState> =
        MutableStateFlow(HomeScreenUiState.Loading(false))

    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    private val _sideEffects: MutableSharedFlow<HomeScreenSideEffects> = MutableSharedFlow()

    val sideEffect: SharedFlow<HomeScreenSideEffects> = _sideEffects.asSharedFlow()

    private val currentState get() = state.replayCache.firstOrNull() ?: HomeScreenState()


    init {
        getPopularContent()
        viewModelScope.launch {
            state.collect { screenState ->
                when (screenState.errorState) {
                    is PopularsErrorState.NoConnection -> if (screenState.errorState.isPagination) {
                        setSideEffect(HomeScreenSideEffects.ShowMessage(StringVO.Resource(R.string.error_no_connection)))
                    } else setErrorUiState(
                        PopularsErrorUiState.UnknownError(
                            StringVO.Resource(R.string.error_no_connection)
                        )
                    )

                    is PopularsErrorState.NotFound -> if (screenState.errorState.isPagination) {
                        setSideEffect(HomeScreenSideEffects.ShowMessage(StringVO.Resource(R.string.error_not_found)))
                    } else setErrorUiState(
                        PopularsErrorUiState.UnknownError(
                            StringVO.Resource(
                                R.string.error_not_found
                            )
                        )
                    )

                    is PopularsErrorState.ServerError -> if (screenState.errorState.isPagination) {
                        setSideEffect(HomeScreenSideEffects.ShowMessage(StringVO.Resource(R.string.error_service_problem)))
                    } else setErrorUiState(
                        PopularsErrorUiState.UnknownError(
                            StringVO.Resource(
                                R.string.error_service_problem
                            )
                        )
                    )

                    is PopularsErrorState.UnknownError -> if (screenState.errorState.isPagination) {
                        setSideEffect(HomeScreenSideEffects.ShowMessage(StringVO.Resource(R.string.error_something_went_wrong)))
                    } else setErrorUiState(
                        PopularsErrorUiState.UnknownError(
                            StringVO.Resource(
                                R.string.error_something_went_wrong
                            )
                        )
                    )

                    PopularsErrorState.NoError -> {
                        when {
                            screenState.isLoading -> setUiStateLoading(currentState.pageToLoad != FIRST_PAGE)
                            else -> setUiStateSuccess(content = screenState.popularContent.content)
                        }
                    }
                }
            }
        }
    }

    fun handleEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.PaginationTriggered -> getPopularContent()
            is HomeScreenEvent.OnContentClicked -> {
                viewModelScope.launch {
                    _sideEffects.emit(
                        HomeScreenSideEffects.NavigateToDetails(
                            contentType = event.content.type, id = event.content.id
                        )
                    )
                }
            }

            HomeScreenEvent.RefreshButtonClicked -> {
                if (isClickDebounce()) {
                    getPopularContent()
                }
            }
        }
    }


    private fun getPopularContent() {
        if (isPaginationDebounce()) {
            viewModelScope.launch {
                getPopularMoviesUseCase(
                    page = currentState.pageToLoad,
                    getLanguageForAppUseCase()
                ).onStart {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            errorState = PopularsErrorState.NoError
                        )
                    }
                }.zip(
                    getPopularTVShowsUseCase(
                        page = currentState.pageToLoad,
                        getLanguageForAppUseCase()
                    )
                ) { popularMovies, popularTVShows ->

                    val contentMutex = Mutex()
                    var pagedList = currentState.popularContent
                    var isNetworkError = false
                    var isMovieFetchingError = false
                    var isTvShowFetchingError = false

                    popularMovies.handle(object :
                        Resource.ResultHandler<PagedList<Content.Movie>, ErrorEntity> {
                        override suspend fun handleSuccess(data: PagedList<Content.Movie>) {
                            contentMutex.withLock {
                                pagedList += contentDomainToUIMapper.toPagedList(
                                    data
                                ) { contentDomainToUIMapper.toPopularContent(it) }
                            }
                        }

                        override suspend fun handleError(errorStatus: ErrorEntity) {
                            when (errorStatus) {
                                is ErrorEntity.NetworksError.NoInternet -> isNetworkError = true
                                else -> isMovieFetchingError = true
                            }
                        }
                    })

                    popularTVShows.handle(object :
                        Resource.ResultHandler<PagedList<Content.TVShow>, ErrorEntity> {
                        override suspend fun handleSuccess(data: PagedList<Content.TVShow>) {
                            contentMutex.withLock {
                                pagedList += contentDomainToUIMapper.toPagedList(
                                    data
                                ) { contentDomainToUIMapper.toPopularContent(it) }
                            }
                        }

                        override suspend fun handleError(errorStatus: ErrorEntity) {
                            when (errorStatus) {
                                is ErrorEntity.NetworksError.NoInternet -> isNetworkError = true
                                else -> isTvShowFetchingError = true
                            }
                        }
                    })

                    return@zip if (isNetworkError) {
                        Resource.Error<PagedList<PopularContentUi>, PopularsErrorState>(
                            PopularsErrorState.NoConnection(
                                currentState.pageToLoad != FIRST_PAGE
                            )
                        )
                    } else if (isMovieFetchingError && isTvShowFetchingError) {
                        Resource.Error<PagedList<PopularContentUi>, PopularsErrorState>(
                            PopularsErrorState.ServerError(
                                currentState.pageToLoad != FIRST_PAGE
                            )
                        )
                    } else {
                        Resource.Success(pagedList)
                    }
                }.collect { data ->
                    data.handle(object :
                        Resource.ResultHandler<PagedList<PopularContentUi>, PopularsErrorState> {
                        override suspend fun handleSuccess(data: PagedList<PopularContentUi>) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    popularContent = data,
                                    pageToLoad = currentState.pageToLoad + NEXT_PAGE
                                )
                            }
                        }

                        override suspend fun handleError(errorStatus: PopularsErrorState) {
                            _state.update {
                                it.copy(isLoading = false, errorState = errorStatus)
                            }
                        }
                    })
                }
            }
        }
    }


    private fun isPaginationDebounce(): Boolean {
        val current = currentState.isPaginationAllowed
        if (currentState.isPaginationAllowed) {
            _state.update {
                it.copy(isPaginationAllowed = false)
            }
            viewModelScope.launch {
                delay(PAGINATION_DEBOUNCE_DELAY_MILLIS)
                _state.update {
                    it.copy(isPaginationAllowed = true)
                }
            }
        }
        return current
    }

    private fun isClickDebounce(): Boolean {
        val current = currentState.isClickAllowed
        if (currentState.isClickAllowed) {
            _state.update {
                it.copy(isClickAllowed = false)
            }
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                _state.update {
                    it.copy(isClickAllowed = true)
                }
            }
        }
        return current
    }

    private fun setErrorUiState(errorState: PopularsErrorUiState) {
        _uiState.update { HomeScreenUiState.Error(errorState) }
    }

    private fun setUiStateSuccess(content: List<PopularContentUi>) {
        _uiState.update { HomeScreenUiState.Success(content) }
    }

    private fun setUiStateLoading(isPagination: Boolean) {
        _uiState.update { HomeScreenUiState.Loading(isPagination) }
    }

    private suspend fun setSideEffect(sideEffect: HomeScreenSideEffects) {
        _sideEffects.emit(sideEffect)
    }

    companion object {
        const val FIRST_PAGE = 1
        const val NEXT_PAGE = 1
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 500L
        const val PAGINATION_DEBOUNCE_DELAY_MILLIS = 1000L
    }

}