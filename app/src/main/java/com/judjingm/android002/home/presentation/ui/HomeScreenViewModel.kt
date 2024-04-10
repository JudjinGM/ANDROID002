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
import com.judjingm.android002.home.presentation.models.PopularContentResult
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
                        setSideEffect(HomeScreenSideEffects.ShowMessage(StringVO.Resource(R.string.error_no_internet)))
                        resetStateToNoError()
                    } else setErrorUiState(
                        PopularsErrorUiState.UnknownError(
                            StringVO.Resource(R.string.error_no_internet)
                        )
                    )

                    is PopularsErrorState.NotFound -> if (screenState.errorState.isPagination) {
                        setSideEffect(HomeScreenSideEffects.ShowMessage(StringVO.Resource(R.string.error_not_found)))
                        resetStateToNoError()
                    } else setErrorUiState(
                        PopularsErrorUiState.UnknownError(
                            StringVO.Resource(
                                R.string.error_not_found
                            )
                        )
                    )

                    is PopularsErrorState.ServerError -> if (screenState.errorState.isPagination) {
                        setSideEffect(HomeScreenSideEffects.ShowMessage(StringVO.Resource(R.string.error_service_problem)))
                        resetStateToNoError()
                    } else setErrorUiState(
                        PopularsErrorUiState.UnknownError(
                            StringVO.Resource(
                                R.string.error_service_problem
                            )
                        )
                    )

                    is PopularsErrorState.UnknownError -> if (screenState.errorState.isPagination) {
                        setSideEffect(HomeScreenSideEffects.ShowMessage(StringVO.Resource(R.string.error_something_went_wrong)))
                        resetStateToNoError()
                    } else setErrorUiState(
                        PopularsErrorUiState.UnknownError(
                            StringVO.Resource(
                                R.string.error_something_went_wrong
                            )
                        )
                    )

                    PopularsErrorState.NoError -> {
                        when {
                            screenState.isLoading -> setUiStateLoading(
                                (currentState.pageToLoadMovies != FIRST_PAGE)
                                        || (currentState.pageToLoadTvShows != FIRST_PAGE)
                            )

                            else -> setUiStateSuccess(content = screenState.resultContent)
                        }
                    }
                }
            }
        }
    }

    fun handleEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.PaginationTriggered -> {
                if (isPaginationDebounce()) {
                    getPopularContent()
                }
            }

            is HomeScreenEvent.OnContentClicked -> {
                viewModelScope.launch {
                    _sideEffects.emit(
                        HomeScreenSideEffects.NavigateToDetails(
                            contentTypeName = event.content.type.name, id = event.content.id
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
        viewModelScope.launch {
            with(currentState) {
                if (pageToLoadTvShows <= totalPagesTvShows
                    && pageToLoadMovies <= totalPagesMovies
                ) {
                    getPopularCombinedContent(pageToLoadMovies, pageToLoadTvShows)
                } else if (pageToLoadTvShows <= totalPagesTvShows) {
                    getPopularMovies(pageToLoadTvShows)
                } else if (pageToLoadMovies <= totalPagesMovies) {
                    getPopularTvShows(pageToLoadMovies)
                }
            }
        }
    }

    private suspend fun proceedResult(
        result: Resource<PopularContentResult, PopularsErrorState>,
    ) {
        result.handle(object :
            Resource.ResultHandler<PopularContentResult, PopularsErrorState> {
            override suspend fun handleSuccess(data: PopularContentResult) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        resultContent = data.items,
                        totalPagesMovies = data.totalPagesMovies,
                        totalPagesTvShows = data.totalPagesTv,
                        pageToLoadMovies = currentState.pageToLoadMovies + NEXT_PAGE,
                        pageToLoadTvShows = currentState.pageToLoadTvShows + NEXT_PAGE,
                        errorState = PopularsErrorState.NoError
                    )
                }
            }

            override suspend fun handleError(error: PopularsErrorState) {
                _state.update {
                    it.copy(isLoading = false, errorState = error)
                }
            }
        })
    }

    private suspend fun getPopularCombinedContent(
        pageToLoadMovies: Int,
        pageToLoadTvShows: Int,
    ) {
        getPopularMoviesUseCase(page = pageToLoadMovies, getLanguageForAppUseCase())
            .onStart { setLoadingOnStart() }
            .zip(
                getPopularTVShowsUseCase(
                    page = pageToLoadTvShows,
                    getLanguageForAppUseCase()
                )
            ) { popularMovies, popularTVShows ->

                val contentMutex = Mutex()
                val items = currentState.resultContent.toMutableList()
                var isNetworkError = false
                var isMovieFetchingError = false
                var isTvShowFetchingError = false
                var isMoviesEmpty = false
                var isTvShowsEmpty = false

                var totalPagesMovies = FIRST_PAGE
                var totalPagesTv = FIRST_PAGE

                handleSearchContentResult(
                    searchContent = popularMovies,
                    searchResult = {
                        contentMutex.withLock { items.addAll(it) }
                    },
                    onEmptyContent = {
                        isMoviesEmpty = true
                    },
                    onServerError = {
                        isMovieFetchingError = true
                    },
                    onNetworkError = { isNetworkError = true },
                    totalPages = {
                        totalPagesMovies = it
                    }

                )

                handleSearchContentResult(
                    searchContent = popularTVShows,
                    searchResult = {
                        contentMutex.withLock { items.addAll(it) }
                    },
                    onEmptyContent = {
                        isTvShowsEmpty = true
                    },
                    onServerError = {
                        isTvShowFetchingError = true
                    },
                    onNetworkError = { isNetworkError = true },
                    totalPages = {
                        totalPagesTv = it
                    }

                )

                return@zip if (isNetworkError) {
                    Resource.Error<PopularContentResult, PopularsErrorState>(
                        PopularsErrorState.NoConnection(
                            currentState.pageToLoadMovies != FIRST_PAGE
                                    && currentState.pageToLoadTvShows != FIRST_PAGE
                        )
                    )
                } else if (isMovieFetchingError && isTvShowFetchingError) {
                    Resource.Error<PopularContentResult, PopularsErrorState>(
                        PopularsErrorState.ServerError(
                            currentState.pageToLoadMovies != FIRST_PAGE
                                    && currentState.pageToLoadTvShows != FIRST_PAGE
                        )
                    )
                } else if (isMoviesEmpty && isTvShowsEmpty) {
                    Resource.Error<PopularContentResult, PopularsErrorState>(
                        PopularsErrorState.NotFound(
                            currentState.pageToLoadMovies != FIRST_PAGE
                                    && currentState.pageToLoadTvShows != FIRST_PAGE
                        )
                    )
                } else {
                    Resource.Success(
                        PopularContentResult(
                            items = items,
                            totalPagesMovies = totalPagesMovies,
                            totalPagesTv = totalPagesTv
                        )
                    )
                }
            }.collect { result ->
                proceedResult(result)
            }
    }

    private suspend fun getPopularMovies(
        pageToLoadMovies: Int
    ) {
        getPopularMoviesUseCase(page = pageToLoadMovies, getLanguageForAppUseCase())
            .onStart { setLoadingOnStart() }
            .collect { popularMovies ->

                val items = currentState.resultContent.toMutableList()
                var isNetworkError = false
                var isMovieFetchingError = false
                var isMoviesEmpty = false
                var totalPagesMovies = FIRST_PAGE

                handleSearchContentResult(
                    searchContent = popularMovies,
                    searchResult = {
                        items.addAll(it)
                    },
                    onEmptyContent = {
                        isMoviesEmpty = true
                    },
                    onServerError = {
                        isMovieFetchingError = true
                    },
                    onNetworkError = { isNetworkError = true },
                    totalPages = {
                        totalPagesMovies = it
                    }

                )

                val data = if (isNetworkError) {
                    Resource.Error<PopularContentResult, PopularsErrorState>(
                        PopularsErrorState.NoConnection(
                            currentState.pageToLoadMovies != FIRST_PAGE
                                    && currentState.pageToLoadTvShows != FIRST_PAGE
                        )
                    )
                } else if (isMovieFetchingError) {
                    Resource.Error<PopularContentResult, PopularsErrorState>(
                        PopularsErrorState.ServerError(
                            currentState.pageToLoadMovies != FIRST_PAGE
                                    && currentState.pageToLoadTvShows != FIRST_PAGE
                        )
                    )
                } else if (isMoviesEmpty) {
                    Resource.Error<PopularContentResult, PopularsErrorState>(
                        PopularsErrorState.NotFound(
                            currentState.pageToLoadMovies != FIRST_PAGE
                                    && currentState.pageToLoadTvShows != FIRST_PAGE
                        )
                    )
                } else {
                    Resource.Success(
                        PopularContentResult(
                            items = items,
                            totalPagesMovies = totalPagesMovies,
                        )
                    )
                }
                proceedResult(data)
            }
    }

    private suspend fun getPopularTvShows(
        pageToLoadTvShows: Int,
    ) {
        getPopularTVShowsUseCase(page = pageToLoadTvShows, getLanguageForAppUseCase())
            .onStart { setLoadingOnStart() }
            .collect { popularTVShows ->

                val items = currentState.resultContent.toMutableList()
                var isNetworkError = false
                var isTvShowFetchingError = false
                var isTvShowsEmpty = false
                var totalPagesTv = FIRST_PAGE

                handleSearchContentResult(
                    searchContent = popularTVShows,
                    searchResult = {
                        items.addAll(it)
                    },
                    onEmptyContent = {
                        isTvShowsEmpty = true
                    },
                    onServerError = {
                        isTvShowFetchingError = true
                    },
                    onNetworkError = { isNetworkError = true },
                    totalPages = {
                        totalPagesTv = it
                    }
                )

                val result = if (isNetworkError) {
                    Resource.Error<PopularContentResult, PopularsErrorState>(
                        PopularsErrorState.NoConnection(
                            currentState.pageToLoadMovies != FIRST_PAGE
                                    && currentState.pageToLoadTvShows != FIRST_PAGE
                        )
                    )
                } else if (isTvShowFetchingError) {
                    Resource.Error<PopularContentResult, PopularsErrorState>(
                        PopularsErrorState.ServerError(
                            currentState.pageToLoadMovies != FIRST_PAGE
                                    && currentState.pageToLoadTvShows != FIRST_PAGE
                        )
                    )
                } else if (isTvShowsEmpty) {
                    Resource.Error<PopularContentResult, PopularsErrorState>(
                        PopularsErrorState.NotFound(
                            currentState.pageToLoadMovies != FIRST_PAGE
                                    && currentState.pageToLoadTvShows != FIRST_PAGE
                        )
                    )
                } else {
                    Resource.Success(
                        PopularContentResult(
                            items = items,
                            totalPagesTv = totalPagesTv
                        )
                    )
                }
                proceedResult(result)
            }
    }


    private suspend fun <T : Content> handleSearchContentResult(
        searchContent: Resource<PagedList<T>, ErrorEntity>,
        searchResult: suspend (List<PopularContentUi>) -> Unit,
        onEmptyContent: () -> Unit,
        onNetworkError: () -> Unit,
        onServerError: () -> Unit,
        totalPages: (Int) -> Unit
    ) {
        searchContent.handle(object :
            Resource.ResultHandler<PagedList<T>, ErrorEntity> {
            override suspend fun handleSuccess(data: PagedList<T>) {
                if (data.content.isNotEmpty()) {
                    val result = contentDomainToUIMapper.toPagedList(
                        data
                    ) { contentDomainToUIMapper.toPopularContent(it) }
                    searchResult.invoke(result.content)
                } else {
                    onEmptyContent.invoke()
                }
                totalPages.invoke(data.totalPages)
            }

            override suspend fun handleError(error: ErrorEntity) {
                when (error) {
                    is ErrorEntity.NetworksError.NoInternet -> onNetworkError.invoke()
                    else -> onServerError.invoke()
                }
            }
        }
        )
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

    private fun resetStateToNoError() {
        _state.update {
            it.copy(
                isLoading = false,
                errorState = PopularsErrorState.NoError
            )
        }
    }

    private fun setLoadingOnStart() {
        _state.update {
            it.copy(
                isLoading = true,
            )
        }
    }

    companion object {
        const val FIRST_PAGE = 1
        const val NEXT_PAGE = 1
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 500L
        const val PAGINATION_DEBOUNCE_DELAY_MILLIS = 1000L
    }

}