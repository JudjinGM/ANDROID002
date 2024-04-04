package com.judjingm.android002.search.presentation.ui

import androidx.lifecycle.viewModelScope
import com.judjingm.android002.R
import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.common.domain.models.Content
import com.judjingm.android002.common.domain.models.ErrorEntity
import com.judjingm.android002.common.ui.BaseViewModel
import com.judjingm.android002.common.utill.Resource
import com.judjingm.android002.common.utill.debounce
import com.judjingm.android002.details.domain.useCase.GetLanguageForAppUseCase
import com.judjingm.android002.home.presentation.models.ContentType
import com.judjingm.android002.home.presentation.models.StringVO
import com.judjingm.android002.home.presentation.ui.ContentDomainToUiMapper
import com.judjingm.android002.search.domain.useCase.GetSearchMoviesUseCase
import com.judjingm.android002.search.domain.useCase.GetSearchTVShowsUseCase
import com.judjingm.android002.search.presentation.models.SearchContentResult
import com.judjingm.android002.search.presentation.models.SearchContentUiItem
import com.judjingm.android002.search.presentation.models.state.SearchErrorState
import com.judjingm.android002.search.presentation.models.state.SearchErrorUiState
import com.judjingm.android002.search.presentation.models.state.SearchScreenEvent
import com.judjingm.android002.search.presentation.models.state.SearchScreenSideEffects
import com.judjingm.android002.search.presentation.models.state.SearchScreenState
import com.judjingm.android002.search.presentation.models.state.SearchScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
class SearchViewModel @Inject constructor(
    private val getSearchMoviesUseCase: GetSearchMoviesUseCase,
    private val getSearchTVShowsUseCase: GetSearchTVShowsUseCase,
    private val getLanguageForAppUseCase: GetLanguageForAppUseCase,
    private val contentDomainToUIMapper: ContentDomainToUiMapper
) : BaseViewModel() {

    private val _state: MutableStateFlow<SearchScreenState> = MutableStateFlow(SearchScreenState())

    private val state: StateFlow<SearchScreenState> = _state.asStateFlow()

    private val _uiState: MutableStateFlow<SearchScreenUiState> =
        MutableStateFlow(SearchScreenUiState.Loading(false))

    val uiState: StateFlow<SearchScreenUiState> = _uiState.asStateFlow()

    private val _sideEffects: MutableSharedFlow<SearchScreenSideEffects> = MutableSharedFlow()

    val sideEffect: SharedFlow<SearchScreenSideEffects> = _sideEffects.asSharedFlow()
    private val currentState get() = state.replayCache.firstOrNull() ?: SearchScreenState()

    private val searchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY_MILLIS, viewModelScope, true) {
            searchContent(it, true)
        }

    private var job: Job? = null


    init {
        viewModelScope.launch {
            state.collect { screenState ->
                when (screenState.errorState) {
                    is SearchErrorState.NoConnection -> if (screenState.errorState.isPagination) {
                        setSideEffect(SearchScreenSideEffects.ShowMessage(StringVO.Resource(R.string.error_no_connection)))
                        resetStateToNoError()
                    } else setErrorUiState(
                        SearchErrorUiState.UnknownError(
                            StringVO.Resource(R.string.error_no_connection)
                        )
                    )

                    is SearchErrorState.NotFound -> if (screenState.errorState.isPagination) {
                        setSideEffect(SearchScreenSideEffects.ShowMessage(StringVO.Resource(R.string.error_not_found)))
                        resetStateToNoError()
                    } else setErrorUiState(
                        SearchErrorUiState.UnknownError(
                            StringVO.Resource(
                                R.string.error_not_found
                            )
                        )
                    )

                    is SearchErrorState.ServerError -> if (screenState.errorState.isPagination) {
                        setSideEffect(SearchScreenSideEffects.ShowMessage(StringVO.Resource(R.string.error_service_problem)))
                        resetStateToNoError()
                    } else setErrorUiState(
                        SearchErrorUiState.UnknownError(
                            StringVO.Resource(
                                R.string.error_service_problem
                            )
                        )
                    )

                    is SearchErrorState.UnknownError -> if (screenState.errorState.isPagination) {
                        setSideEffect(SearchScreenSideEffects.ShowMessage(StringVO.Resource(R.string.error_something_went_wrong)))
                        resetStateToNoError()
                    } else setErrorUiState(
                        SearchErrorUiState.UnknownError(
                            StringVO.Resource(
                                R.string.error_something_went_wrong
                            )
                        )
                    )

                    SearchErrorState.NoError -> {
                        when {
                            screenState.isLoading -> setUiStateLoading(
                                (currentState.pageToLoadMovies != SearchScreenState.FIRST_PAGE)
                                        || (currentState.pageToLoadTvShows != SearchScreenState.FIRST_PAGE)
                            )

                            else -> setUiStateSuccess(content = screenState.resultContent)
                        }
                    }
                }
            }
        }
    }

    fun handleEvent(event: SearchScreenEvent) {
        when (event) {
            is SearchScreenEvent.OnContentClicked -> {
                if (isClickDebounce()) {
                    viewModelScope.launch {
                        _sideEffects.emit(
                            SearchScreenSideEffects.NavigateToDetails(
                                event.contentId, event.contentType.name
                            )
                        )
                    }
                }
            }

            is SearchScreenEvent.OnSearchQueryChanged -> {
                searchDebounced(event.query)
            }

            SearchScreenEvent.PaginationTriggered -> {
                if (isPaginationDebounce()
                    && currentState.query.isNotBlank()
                    && (currentState.totalPagesMovies >= currentState.pageToLoadMovies
                            || currentState.totalPagesTvShows >= currentState.pageToLoadTvShows)
                ) {
                    searchContent(currentState.query, false)
                }
            }

            SearchScreenEvent.OnSearchQueryCleared -> {
                searchDebounce(BLANC_STRING)
                job?.cancel()

            }
        }
    }

    private fun searchDebounced(query: String) {
        if (query == currentState.query) {
            return
        }
        if (query.isBlank()) {
            _state.update {
                it.copy(
                    query = query, isLoading = false, resultContent = emptyList()
                )
            }
        } else {
            searchDebounce(query)
        }
    }

    private fun searchContent(query: String, isNewSearch: Boolean) {
        val pageToLoadMovies = if (isNewSearch) FIRST_PAGE else currentState.pageToLoadMovies
        val pageToLoadTvShows = if (isNewSearch) FIRST_PAGE else currentState.pageToLoadTvShows

        job = viewModelScope.launch {
            if (query.isNotBlank()) {
                if (pageToLoadTvShows <= currentState.totalPagesTvShows
                    && pageToLoadMovies <= currentState.totalPagesMovies
                ) {
                    searchCombinedContent(query, pageToLoadMovies, pageToLoadTvShows, isNewSearch)
                } else if (pageToLoadTvShows <= currentState.totalPagesTvShows) {
                    searchTvShowContent(query, pageToLoadTvShows, isNewSearch)
                } else if (pageToLoadMovies <= currentState.totalPagesMovies) {
                    searchMovieContent(query, pageToLoadMovies, isNewSearch)
                }
            }
        }
    }

    private suspend fun proceedResult(
        query: String, data: Resource<SearchContentResult, SearchErrorState>
    ) {
        data.handle(object : Resource.ResultHandler<SearchContentResult, SearchErrorState> {

            override suspend fun handleSuccess(data: SearchContentResult) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        query = query,
                        resultContent = data.items,
                        totalPagesMovies = data.totalPagesMovies,
                        totalPagesTvShows = data.totalPagesTv,
                        pageToLoadMovies = currentState.pageToLoadMovies + NEXT_PAGE,
                        pageToLoadTvShows = currentState.pageToLoadTvShows + NEXT_PAGE,
                        errorState = SearchErrorState.NoError
                    )
                }
            }

            override suspend fun handleError(errorStatus: SearchErrorState) {
                _state.update {
                    it.copy(
                        isLoading = false, errorState = errorStatus
                    )
                }
            }
        })
    }

    private suspend fun searchCombinedContent(
        query: String,
        pageToLoadMovies: Int,
        pageToLoadTvShows: Int,
        isNewSearch: Boolean
    ) {
        getSearchMoviesUseCase(
            query, pageToLoadMovies, getLanguageForAppUseCase()
        )
            .onStart { onSearchContentStart(isNewSearch) }
            .zip(
                getSearchTVShowsUseCase(
                    query, pageToLoadTvShows, getLanguageForAppUseCase()
                )
            ) { searchMovies, searchTVShows ->
                val contentMutex = Mutex()
                val items: MutableList<SearchContentUiItem> =
                    currentState.resultContent.toMutableList()

                var isNetworkError = false
                var isMovieFetchingError = false
                var isTvShowFetchingError = false
                var isMoviesEmpty = false
                var isTvShowsEmpty = false

                var totalPagesMovies = FIRST_PAGE
                var totalPagesTv = FIRST_PAGE

                handleSearchContentResult(
                    contentType = ContentType.MOVIE,
                    searchContent = searchMovies,
                    contentMutex = contentMutex,
                    items,
                    onEmptyContent = {
                        isMoviesEmpty = true
                    },
                    onNetworkError = {
                        isNetworkError = true
                    },
                    onServerError = {
                        isMovieFetchingError = true
                    },
                    totalPages = {
                        totalPagesMovies = it
                    }
                )

                handleSearchContentResult(
                    contentType = ContentType.SERIES,
                    searchContent = searchTVShows,
                    contentMutex = contentMutex,
                    items,
                    onEmptyContent = {
                        isTvShowsEmpty = true
                    },
                    onNetworkError = {
                        isNetworkError = true
                    },
                    onServerError = {
                        isTvShowFetchingError = true
                    },
                    totalPages = {
                        totalPagesTv = it
                    }
                )

                return@zip if (isNetworkError) {
                    Resource.Error<SearchContentResult, SearchErrorState>(
                        SearchErrorState.NoConnection(
                            currentState.pageToLoadMovies != SearchScreenState.FIRST_PAGE
                                    && currentState.pageToLoadTvShows != SearchScreenState.FIRST_PAGE
                        )
                    )
                } else if (isMovieFetchingError && isTvShowFetchingError) {
                    Resource.Error<SearchContentResult, SearchErrorState>(
                        SearchErrorState.ServerError(
                            currentState.pageToLoadMovies != SearchScreenState.FIRST_PAGE
                                    && currentState.pageToLoadTvShows != SearchScreenState.FIRST_PAGE
                        )
                    )
                } else if (isMoviesEmpty && isTvShowsEmpty) {
                    Resource.Error<SearchContentResult, SearchErrorState>(
                        SearchErrorState.NotFound(
                            currentState.pageToLoadMovies != SearchScreenState.FIRST_PAGE
                                    && currentState.pageToLoadTvShows != SearchScreenState.FIRST_PAGE
                        )
                    )
                } else {
                    Resource.Success(
                        SearchContentResult(
                            items = items.toList(),
                            totalPagesMovies = totalPagesMovies,
                            totalPagesTv = totalPagesTv
                        )
                    )
                }

            }
            .collect { data ->
                proceedResult(query, data)
            }
    }

    private suspend fun searchMovieContent(
        query: String,
        pageToLoadMovies: Int,
        isNewSearch: Boolean
    ) {
        getSearchMoviesUseCase(
            query, pageToLoadMovies, getLanguageForAppUseCase()
        )
            .onStart { onSearchContentStart(isNewSearch) }
            .collect { searchMovies ->
                val items: MutableList<SearchContentUiItem> =
                    if (isNewSearch) mutableListOf() else currentState.resultContent.toMutableList()
                var isNetworkError = false
                var isMovieFetchingError = false
                var isMoviesEmpty = false
                var totalPagesMovies = FIRST_PAGE

                handleSearchContentResult(
                    contentType = ContentType.MOVIE,
                    searchContent = searchMovies,
                    contentMutex = Mutex(),
                    items,
                    onEmptyContent = {
                        isMoviesEmpty = true
                    },
                    onNetworkError = {
                        isNetworkError = true
                    },
                    onServerError = {
                        isMovieFetchingError = true
                    },
                    totalPages = {
                        totalPagesMovies = it

                    }
                )

                val data = if (isNetworkError) {
                    Resource.Error<SearchContentResult, SearchErrorState>(
                        SearchErrorState.NoConnection(
                            currentState.pageToLoadMovies != SearchScreenState.FIRST_PAGE
                                    && currentState.pageToLoadTvShows != SearchScreenState.FIRST_PAGE
                        )
                    )
                } else if (isMovieFetchingError) {
                    Resource.Error<SearchContentResult, SearchErrorState>(
                        SearchErrorState.ServerError(
                            currentState.pageToLoadMovies != SearchScreenState.FIRST_PAGE
                                    && currentState.pageToLoadTvShows != SearchScreenState.FIRST_PAGE
                        )
                    )
                } else if (isMoviesEmpty) {
                    Resource.Error<SearchContentResult, SearchErrorState>(
                        SearchErrorState.NotFound(
                            currentState.pageToLoadMovies != SearchScreenState.FIRST_PAGE
                                    && currentState.pageToLoadTvShows != SearchScreenState.FIRST_PAGE
                        )
                    )
                } else {
                    Resource.Success(
                        SearchContentResult(
                            items = items,
                            totalPagesMovies = totalPagesMovies,
                            totalPagesTv = FIRST_PAGE
                        )
                    )
                }
                proceedResult(query, data)
            }
    }

    private suspend fun searchTvShowContent(query: String, pageToLoad: Int, isNewSearch: Boolean) {
        getSearchTVShowsUseCase(
            query, pageToLoad, getLanguageForAppUseCase()
        )
            .onStart { onSearchContentStart(isNewSearch) }
            .collect { searchTVShows ->
                val items: MutableList<SearchContentUiItem> =
                    if (isNewSearch) mutableListOf() else currentState.resultContent.toMutableList()

                var isNetworkError = false
                var isTvShowFetchingError = false
                var isTvShowsEmpty = false
                var tvShowTotalPages = 1

                handleSearchContentResult(
                    contentType = ContentType.SERIES,
                    searchContent = searchTVShows,
                    contentMutex = Mutex(),
                    items,
                    onEmptyContent = {
                        isTvShowsEmpty = true
                    },
                    onNetworkError = {
                        isNetworkError = true
                    },
                    onServerError = {
                        isTvShowFetchingError = true
                    },
                    totalPages = {
                        tvShowTotalPages = it
                    }
                )

                val data = if (isNetworkError) {
                    Resource.Error<SearchContentResult, SearchErrorState>(
                        SearchErrorState.NoConnection(
                            currentState.pageToLoadMovies != SearchScreenState.FIRST_PAGE
                                    && currentState.pageToLoadTvShows != SearchScreenState.FIRST_PAGE
                        )
                    )
                } else if (isTvShowFetchingError) {
                    Resource.Error<SearchContentResult, SearchErrorState>(
                        SearchErrorState.ServerError(
                            currentState.pageToLoadMovies != SearchScreenState.FIRST_PAGE
                                    && currentState.pageToLoadTvShows != SearchScreenState.FIRST_PAGE
                        )
                    )
                } else if (isTvShowsEmpty) {
                    Resource.Error<SearchContentResult, SearchErrorState>(
                        SearchErrorState.NotFound(
                            currentState.pageToLoadMovies != SearchScreenState.FIRST_PAGE
                                    && currentState.pageToLoadTvShows != SearchScreenState.FIRST_PAGE
                        )
                    )
                } else {
                    Resource.Success(
                        data =
                        SearchContentResult(
                            totalPagesMovies = 1,
                            totalPagesTv = tvShowTotalPages,
                            items = items.toList()
                        )
                    )
                }

                proceedResult(query, data)
            }
    }

    private suspend fun <T : Content> handleSearchContentResult(
        contentType: ContentType,
        searchContent: Resource<PagedList<T>, ErrorEntity>,
        contentMutex: Mutex,
        items: MutableList<SearchContentUiItem>,
        onEmptyContent: () -> Unit,
        onNetworkError: () -> Unit,
        onServerError: () -> Unit,
        totalPages: (Int) -> Unit
    ) {

        val title = when (contentType) {
            ContentType.MOVIE -> StringVO.Resource(
                R.string.movies
            )

            ContentType.SERIES -> StringVO.Resource(
                R.string.tv_shows
            )

            ContentType.UNKNOWN -> StringVO.Plain(
                BLANC_STRING
            )
        }
        searchContent.handle(object :
            Resource.ResultHandler<PagedList<T>, ErrorEntity> {
            override suspend fun handleSuccess(data: PagedList<T>) {
                contentMutex.withLock {
                    if (data.content.isNotEmpty()) {
                        items.add(
                            SearchContentUiItem.TitleUiItem(
                                id = items.size, title = title
                            )
                        )
                        items.addAll(data.content.map { items ->
                            contentDomainToUIMapper.toSearchContentUiItem(
                                items
                            )
                        })
                    } else {
                        onEmptyContent.invoke()
                    }
                }
                totalPages.invoke(data.totalPages)
            }

            override suspend fun handleError(errorStatus: ErrorEntity) {
                when (errorStatus) {
                    is ErrorEntity.NetworksError.NoInternet -> onNetworkError.invoke()
                    else -> onServerError.invoke()
                }
            }

        })
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


    private fun onSearchContentStart(isNewSearch: Boolean) {
        if (isNewSearch) {
            _state.update {
                it.copy(
                    isLoading = true,
                    resultContent = emptyList(),
                    pageToLoadMovies = FIRST_PAGE,
                    pageToLoadTvShows = FIRST_PAGE,
                    totalPagesMovies = FIRST_PAGE,
                    totalPagesTvShows = FIRST_PAGE
                )
            }
        } else {
            _state.update {
                it.copy(
                    isLoading = true,
                )
            }
        }
    }

    private fun setErrorUiState(errorState: SearchErrorUiState) {
        _uiState.update { SearchScreenUiState.Error(errorState) }
    }

    private fun setUiStateSuccess(content: List<SearchContentUiItem>) {
        _uiState.update { SearchScreenUiState.Success(content) }
    }

    private fun setUiStateLoading(isPagination: Boolean) {
        _uiState.update { SearchScreenUiState.Loading(isPagination) }
    }

    private suspend fun setSideEffect(sideEffect: SearchScreenSideEffects) {
        _sideEffects.emit(sideEffect)
    }

    private fun resetStateToNoError() {
        _state.update {
            it.copy(
                isLoading = false,
                errorState = SearchErrorState.NoError
            )
        }
    }

    companion object {
        const val FIRST_PAGE = 1
        const val NEXT_PAGE = 1
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 500L
        const val PAGINATION_DEBOUNCE_DELAY_MILLIS = 1000L
        const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        const val BLANC_STRING = ""
    }
}