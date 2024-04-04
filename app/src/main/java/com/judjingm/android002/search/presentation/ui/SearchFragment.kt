package com.judjingm.android002.search.presentation.ui

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.judjingm.android002.R
import com.judjingm.android002.common.ui.BaseFragment
import com.judjingm.android002.databinding.FragmentSearchBinding
import com.judjingm.android002.search.presentation.models.SearchContentUiItem
import com.judjingm.android002.search.presentation.models.state.SearchErrorUiState
import com.judjingm.android002.search.presentation.models.state.SearchScreenEvent
import com.judjingm.android002.search.presentation.models.state.SearchScreenSideEffects
import com.judjingm.android002.search.presentation.models.state.SearchScreenUiState
import com.judjingm.android002.search.presentation.recycleView.RecycleViewSearchContentAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment :
    BaseFragment<FragmentSearchBinding, SearchViewModel>(FragmentSearchBinding::inflate) {
    override val viewModel: SearchViewModel by viewModels()
    private var adapter: RecycleViewSearchContentAdapter? = null
    private var inputMethodManager: InputMethodManager? = null


    override fun onConfigureViews() {
        recycleViewInit()
        setOnScrollForRecycleView(binding.searchRecyclerView, adapter, viewModel)
        setOnTextChangeListeners()

        binding.searchFormButton.setOnClickListener {
            binding.searchEditText.setText(DEFAULT_TEXT)
            viewModel.handleEvent(SearchScreenEvent.OnSearchQueryCleared)
        }

        inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(
            binding.searchEditText.windowToken, 0
        )
    }

    override fun onSubscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    updateUi(uiState)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sideEffect.collect {
                    handleSideEffects(it)
                }
            }
        }
    }

    private fun updateUi(uiState: SearchScreenUiState) {
        uiState.handleState(
            handler = object : SearchScreenUiState.StateHandler {

                override fun handleLoading(isPagination: Boolean) {
                    showLoading(isPagination)
                }

                override fun handleSuccess(content: List<SearchContentUiItem>) {
                    showContent(content)
                }

                override fun handleError(error: SearchErrorUiState) {
                    when (error) {
                        is SearchErrorUiState.CouldNotFetchData -> showError(
                            error.message.value(requireContext())
                        )

                        is SearchErrorUiState.NoConnection -> showErrorNoConnection(
                            error.message.value(requireContext())
                        )

                        is SearchErrorUiState.NothingFound -> showError(
                            error.message.value(requireContext())
                        )

                        is SearchErrorUiState.UnknownError -> showError(
                            error.message.value(requireContext())
                        )
                    }

                }
            }
        )
    }

    private fun handleSideEffects(sideEffect: SearchScreenSideEffects) {
        when (sideEffect) {
            is SearchScreenSideEffects.ShowMessage -> {
                showToast(sideEffect.message.value(requireContext()))
                binding.paginationProgressBar.isVisible = false
            }

            is SearchScreenSideEffects.NavigateToDetails -> {
                val direction =
                    SearchFragmentDirections.actionSearchFragmentToContentDetailFragment(
                        contentId = sideEffect.id,
                        contentTypeName = sideEffect.contentTypeName
                    )
                findNavController().navigate(direction)
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    private fun recycleViewInit() {
        adapter = RecycleViewSearchContentAdapter { content ->
            viewModel.handleEvent(
                SearchScreenEvent.OnContentClicked(
                    content.id,
                    content.type
                )
            )
        }

        binding.searchRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.searchRecyclerView.adapter = adapter
    }

    private fun setOnScrollForRecycleView(
        recyclerView: RecyclerView,
        adapter: RecycleViewSearchContentAdapter?,
        viewModel: SearchViewModel
    ) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val pos =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = adapter?.itemCount
                    if (itemsCount != null && pos >= (itemsCount - 1)) {
                        viewModel.handleEvent(SearchScreenEvent.PaginationTriggered)
                    }
                }
            }
        })
    }

    private fun setOnTextChangeListeners() {
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            setSearchEditTextEndDrawable(text)
            if (text.isNullOrEmpty()) {
                viewModel.handleEvent(SearchScreenEvent.OnSearchQueryCleared)
            }
            viewModel.handleEvent(
                SearchScreenEvent.OnSearchQueryChanged(
                    text?.toString() ?: DEFAULT_TEXT
                )
            )
        }
    }


    private fun setSearchEditTextEndDrawable(charSequence: CharSequence?) {
        if (charSequence.isNullOrEmpty()) {
            binding.searchFormButton.setImageResource(R.drawable.ic_search)
        } else {
            binding.searchFormButton.setImageResource(R.drawable.ic_cross)
        }
    }

    private fun showContent(content: List<SearchContentUiItem>) {
        showEmpty()
        adapter?.items = content
    }

    private fun showLoading(isPagination: Boolean) {
        inputMethodManager?.hideSoftInputFromWindow(
            binding.searchEditText.windowToken, 0
        )
        if (isPagination) {
            binding.paginationProgressBar.isVisible = true
        } else {
            showEmpty()
            binding.progressBar.isVisible = true
        }
    }

    private fun showError(text: String) {
        showEmpty()
        adapter?.items = emptyList()
        binding.placeholderImage.isVisible = true
        binding.errorTextView.isVisible = true
        binding.errorTextView.text = text
    }

    private fun showErrorNoConnection(text: String) {
        showEmpty()
        adapter?.items = emptyList()
        binding.placeholderImage.setImageResource(R.drawable.no_signal_no_background)
        binding.placeholderImage.isVisible = true
        binding.errorTextView.isVisible = true
        binding.errorTextView.text = text
    }


    private fun showEmpty() {
        binding.placeholderImage.isVisible = false
        binding.errorTextView.isVisible = false
        binding.progressBar.isVisible = false
        binding.paginationProgressBar.isVisible = false
    }

    companion object {
        const val DEFAULT_TEXT = ""

    }

}